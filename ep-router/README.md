# Enterprise Proxy (Chain Router Module)
When using multiple service chains, it is a module that can call RPC and Transaction to specific service chain node according to purposes. ServiceChainHost registered in the DB is blocked to prevent connection problems through Healthcheck.



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
- COLUMN information
  - ServiceChain

    | column     | comment                                                   |
    | ---------- | --------------------------------------------------------- |
    | name       | name of the Service Chain                                          |
    | owner      | operator name of the Service Chain                                     |
    | is_use     | if the Service Chain is used (0 : not used, 1 : used)              |
    | start_date | date when the Service Chain operation started (0000-00-00 00:00:00)   |
    | end_date   | date when the Service Chain operation ended (0000-00-00 00:00:00) |
    | register   | date the information was registered                                      |
    | modify     | date the information was modified                                  |
    | ip_addr    | IP address which registered or modified the information              |

  - ServiceChainHost

    | column          | comment                                                      |
    | --------------- | ------------------------------------------------------------ |
    | servicechain_id | registration ID for the Service Chain<br />(You can register multiple service chain hosts based on the service chain ID.) |
    | host            | Service Chain Host info                                      |
    | port            | port info for the Service Chain Host                               |
    | is_use          | if the Service Chain Host is in use (0: Not used, 1: used)      |
    | category        | category of the Service Chain Host (Game, Social, eCommerce, ...) |
    | rpc_type        | RPC TYPE which the Service Chin Host supports (0 : json_rpc, 1 : gRPC) |
    | is_alive        | if the Service Chain Host is alive                                |
    | register        | date the Service Chain Host was registered                          |
    | modify          | date the Service Chain Host was modified                       |
    | ip_addr         | IP address which registered or modified the information           |

  - ServiceChainHostHealthCheck

    | column        | comment                        |
    | ------------- | ------------------------------ |
    | chain_host_id | registration ID for the Service Chain Host |
    | check_type    | type of the check (0 : Health Check)   |
    | comment       | information of the check result                 |
    | checkdate     | date the performance was performed                     |

---

### CONFIGURATION
- RouterAsyncConfig
  - @Async configuration information which are used when storing Health Check logs into DB.
  - You can configure ThreadPoolTaskExecutor options according to your dev or prod environments.
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
  - properties configuration
    ```
    # Chain Router Async Configure
    chain.router.async.poolSize=100
    chain.router.async.maxPoolSize=100
    chain.router.async.queueCapacity=50
    chain.router.async.keepAliveSeconds=30
    ```

---

### SCHEDULER
- Service Chain Host Health Check is scheduled to be run every **3 seconds**.
- You can call it when it's normalized after 3 seconds.
- Health Check logs are stored in DB for operation purposes.
- You can modify **HealthCheckScheduler.java** according to your dev or prod environments, if necessary.
- The actual execution of Scheduler is managed in RouterHandler.java in EP-API so that you can modify according to your dev or prod environments.
- When it fails to get the response for Alive check, it retries according to EP-RPC Feign configuration.

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
