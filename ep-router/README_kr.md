# Enterprise Proxy (Chain Router Module)
다수의 Service Chain을 사용하는 경우 목적에 맞추어서 특정 Service Chain Node로 RPC 및 Transaction을 호출할수 있는 모듈입니다. DB에 등록된 ServiceChainHost는 Healthcheck를 통해 접속 문제가 있는 경우 차단하도록 되어있습니다.



### TAEBLE SCHEMA
- H2
  ```sql
  CREATE TABLE IF NOT EXISTS ServiceChain (
      id int AUTO_INCREMENT PRIMARY KEY,
      name varchar(250),
      owner varchar(250),
      is_use smallint,
      start_date timestamp,
      end_date timestamp,
      register timestamp,
      modify timestamp,
      ip_addr varchar(50)
  );
  
  CREATE TABLE IF NOT EXISTS ServiceChainHost (
      id int AUTO_INCREMENT PRIMARY KEY,
      servicechain_id int,
      host varchar(150),
      port smallint,
      is_use smallint,
      category varchar,
      rpc_type smallint,
      is_alive smallint,
      register timestamp,
      modify timestamp,
      ip_addr varchar(50)
  );
  
  CREATE TABLE IF NOT EXISTS ServiceChainHostHealthCheck (
      id int AUTO_INCREMENT PRIMARY KEY,
      chain_host_id int,
      check_type smallint,
      comment clob,
      checkdate timestamp
  );
  ```
- Mariadb
  ```sql
  CREATE TABLE IF NOT EXISTS ServiceChain (
      id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
      name VARCHAR(250) NOT NULL DEFAULT '',
      owner VARCHAR(250) NOT NULL DEFAULT '',
      is_use SMALLINT(1) NOT NULL DEFAULT 0,
      start_date TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
      end_date TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
      register TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      modify TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
      ip_addr VARCHAR(50) NOT NULL DEFAULT '',
      PRIMARY KEY(id)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
  
  CREATE TABLE IF NOT EXISTS ServiceChainHost (
      id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
      servicechain_id INT(11) NOT NULL DEFAULT 0,
      host VARCHAR(150) NOT NULL DEFAULT '',
      port SMALLINT(11) NOT NULL DEFAULT 0,
      is_use SMALLINT(1) NOT NULL DEFAULT 0,
      category VARCHAR(150) NOT NULL DEFAULT '',
      rpc_type SMALLINT(11) NOT NULL DEFAULT 1,
      is_alive SMALLINT(1) NOT NULL DEFAULT 0,
      register TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      modify TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
      ip_addr VARCHAR(50) NOT NULL DEFAULT '',
      PRIMARY KEY(id)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
  
  CREATE TABLE ServiceChainHostHealthCheck (
      id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
      chain_host_id INT(11) NOT NULL DEFAULT 0,
      check_type SMALLINT(11) NOT NULL DEFAULT 0,
      comment TEXT NOT NULL DEFAULT '',
      checkdate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      PRIMARY KEY(id)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
  ```
- COLUMN 정보
  - ServiceChain

    | column     | comment                                                   |
    | ---------- | --------------------------------------------------------- |
    | name       | 서비스 체인 명                                            |
    | owner      | 서비스 체인 운영자 명                                     |
    | is_use     | 서비스 체인 사용 유무 (0 : not use, 1 : use)              |
    | start_date | 서비스 체인 서비스 운영 시작 날짜 (0000-00-00 00:00:00)   |
    | end_date   | 서비스 체인 서비스 운영 마지막 날짜 (0000-00-00 00:00:00) |
    | register   | 정보 등록된 날짜                                          |
    | modify     | 정보 수정된 날짜                                          |
    | ip_addr    | 정보 등록 및 수정한 아이피 어드레스                       |

  - ServiceChainHost

    | column          | comment                                                      |
    | --------------- | ------------------------------------------------------------ |
    | servicechain_id | 서비스체인 등록 아이디 <br />(서비스 체인 아이디 기준으로 여러개의 서비스체인호스트를 등록할 수 있습니다.) |
    | host            | 서비스 체인 호스트 정보                                      |
    | port            | 서비스 체인 호스트의 포트 정보                               |
    | is_use          | 서비스 체인 호스트 사용 유무 (0 : 사용안함, 1 : 사용함)      |
    | category        | 서비스 체인 호스트 서비스 카테고리 (게임,소셜,이커머스 등등) |
    | rpc_type        | 서비스 체인 호스트에서 지원하는 RPC TYPE (0 : json_rpc, 1 : gRPC) |
    | is_alive        | 서비스 체인 호스트 Alive 유무                                |
    | register        | 서비스 체인 호스트 등록 날짜                                 |
    | modify          | 서비스 체인 호스트 수정 날짜                                 |
    | ip_addr         | 정보 등록 및 수정한 아이피 어드레스                          |

  - ServiceChainHostHealthCheck

    | column        | comment                        |
    | ------------- | ------------------------------ |
    | chain_host_id | 서비스 체인 호스트 등록 아이디 |
    | check_type    | 체크 종류 (0 : Health Check)   |
    | comment       | 체크 결과 정보                 |
    | checkdate     | 체크 날짜                      |

---

### CONFIGURE
- RouterAsyncConfig
  - Health Check 로그 DB 저장시 사용되는 @Async 설정 정보
  - ThreadPoolTaskExecutor 옵션 정보는 개발 및 배포 환경에 맞추어 설정하시면 됩니다.
    ```java
    @Configurable
    @EnableAsync
    public class RouterAsyncConfig implements AsyncConfigurer {
        /**
         * Executor Pool Size
         * @number
         */
        @Value("${chain.router.async.poolSize:10}")
        private int poolSize;
    
        /**
         * Executor Max Pool Size
         * @number
         */
        @Value("${chain.router.async.maxPoolSize:10}")
        private int maxPoolSize;
    
        /**
         * Executor Queue Capacity
         * @number
         */
        @Value("${chain.router.async.queueCapacity:10}")
        private int queueCapacity;
    
        /**
         * Executor Keep Alive Seconds
         * @number
         */
        @Value("${chain.router.async.keepAliveSeconds:10}")
        private int keepAliveSeconds;
    
        /**
         * Router Async Executor initialize
         * @return
         */
        @Bean(name="routerAsyncExecutor")
        @Override
        public Executor getAsyncExecutor() {
            ThreadPoolTaskExecutor routerAsyncExecutor = new ThreadPoolTaskExecutor();
            routerAsyncExecutor.setCorePoolSize(poolSize);
            routerAsyncExecutor.setMaxPoolSize(maxPoolSize);
            routerAsyncExecutor.setQueueCapacity(queueCapacity);
            routerAsyncExecutor.setKeepAliveSeconds(keepAliveSeconds);
            routerAsyncExecutor.setAllowCoreThreadTimeOut(true);
            routerAsyncExecutor.setWaitForTasksToCompleteOnShutdown(true);
            routerAsyncExecutor.setThreadNamePrefix("ROUTER-ASYNC-");
            routerAsyncExecutor.initialize();
            return routerAsyncExecutor;
        }
      
        @Override
        public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
            return new RouterAsyncExceptionHandler();
        }
    }
    ```
  - properties 설정
    ```
    # Chain Router Async Configure
    chain.router.async.poolSize=100
    chain.router.async.maxPoolSize=100
    chain.router.async.queueCapacity=50
    chain.router.async.keepAliveSeconds=30
    ```

---

### SCHEDULER
- 서비스 체인 호스트 Health Check는 **3초** 마다 확인하도록 되어있습니다.
- 3초 이후에 정상화 되면 호출 가능합니다.
- Health Check 로그는 운영을 위해 DB에 저장하도록 되어있습니다.
- 개발 및 배포 환경에 맞추어 수정이 필요한 경우 **HealthCheckScheduler.java** 수정하시면 됩니다.
- 실제 Scheduler를 실행하는곳은 EP-API의 RouterHandler.java에서 관리하고 있으나 개발 및 배포 환경에 따라 수정하셔도 됩니다.
- Alive 체크시 정상적으로 response를 받지 않은 경우 EP-RPC Feign설정에 따라 재시도 하게 됩니다.

---

### CHAIN INFO STATUS CODE
```java
UNKNOWN(999,"UNKNOWN")
PARAMETER_INVALID(998,"CHAIN HOST PARAMETER INVALID")
CHAIN_EXPIRE(997,"SERVICE CHAIN USE EXPIRE")
CHAIN_IS_NOT_EXIST(996,"SERVICE CHAIN IS NOT EXIST")
CHAIN_IS_NOT_USE(995,"SERVICE CHAIN IS NOT USE")
....
CHAIN_HOST_IS_NOT_USE(899,"SERVICE CHAIN HOST IS NOT USE")
CHAIN_HOST_IS_NOT_ALIVE(898,"SERVICE CHAIN HOST IS NOT ALIVE")
CHAIN_HOST_IS_NOT_EXIST(897,"SERVICE CHAIN HOST IS NOT EXIST")
....
CHAIN_INSERT_FAIL(799,"SERVICE CHAIN INSERT FAIL")
CHAIN_HOST_INSERT_FAIL(798,"SERVICE CHAIN HOST INSERT FAIL")
CHAIN_UPDATE_FAIL(797,"SERVICE CHAIN UPDATE FAIL")
CHAIN_HOST_UPDATE_FAIL(796,"SERVICE CHAIN HOST UPDATE FAIL")
CHAIN_DELETE_FAIL(795,"SERVICE CHAIN DELETE FAIL")
CHAIN_HOST_DELETE_FAIL(794,"SERVICE CHAIN HOST DELETE FAIL")
....
SUCCESS(0,"SUCCESS")
```

---

### QUERY MAPPER
- SERVICE CHAIN QUERY XML
  - namespace : com.klaytn.enterpriseproxy.mapper.servicechain
  - resources/mapper/chain/servicechain.xml
- HEALTH CHECKER QUERY XML
  - namespace : com.klaytn.enterpriseproxy.mapper.healthchecker
  - resources/mapper/health/health_checker.xml