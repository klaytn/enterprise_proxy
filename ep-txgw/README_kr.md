# Enterprise Proxy (TXGW Module)
리퀘스트 메타 정보등을 DB 저장하여 추후 운영 및 데이터 분석용으로 사용할수 있도록 하는 모듈입니다. DB 저장시 암호화 처리 및 더 필요한 정보가 있다면 개발 및 배포 환경에 따라 수정하시면 됩니다.



### TABLE SCHEMA
- H2

  ```sql
  CREATE TABLE IF NOT EXISTS TxGatewayLog (
      id int AUTO_INCREMENT PRIMARY KEY ,
      method varchar(100),
      query_string clob,
      request_uri varchar(250),
      servlet_path varchar(250),
      path_info varchar(250),
      header_host varchar(250),
      header_user_agent clob,
      header_accept varchar(150),
      header_accept_language varchar(250),
      header_accept_encoding varchar(250),
      header_referer varchar(250),
      header_rpccaller varchar(100),
      header_origin varchar(250),
      header_connection varchar(150),
      header_content_length int,
      character_encoding varchar(150),
      content_length int,
      content_type varchar(150),
      local_addr varchar(45),
      locale varchar(100),
      local_name varchar(150),
      local_port varchar(20),
      protocol varchar(150),
      remote_addr varchar(45),
      remote_host varchar(150),
      remote_port varchar(20),
      scheme varchar(100),
      server_name varchar(250),
      server_port varchar(20),
      request_params clob,
      register timestamp
  );
  ```
  
- Mariadb

  ```sql
  CREATE TABLE TxGatewayLog (
        id                                  INT(11) UNSIGNED AUTO_INCREMENT,
        method                              VARCHAR(100) DEFAULT '',
        query_string                        MEDIUMTEXT,
        request_uri                         VARCHAR(250) DEFAULT '',
        servlet_path                        VARCHAR(250) DEFAULT '',
        path_info                           VARCHAR(250) DEFAULT '',
        header_host                         VARCHAR(250) DEFAULT '',
        header_user_agent                   TINYTEXT,
        header_accept                       VARCHAR(150) DEFAULT '',
        header_accept_language              VARCHAR(250) DEFAULT '',
        header_accept_encoding              VARCHAR(250) DEFAULT '',
        header_referer                      VARCHAR(250) DEFAULT '',
        header_rpccaller                    VARCHAR(100) DEFAULT '',
        header_origin                       VARCHAR(250) DEFAULT '',
        header_connection                   VARCHAR(150) DEFAULT '',
        header_content_length               INT(11) DEFAULT 0,
        character_encoding                  VARCHAR(150) DEFAULT '',
        content_length                      INT(11) DEFAULT 0,
        content_type                        VARCHAR(150) DEFAULT '',
        local_addr                          VARCHAR(45) DEFAULT '127.0.0.1',
        locale                              VARCHAR(100) DEFAULT '',
        local_name                          VARCHAR(150) DEFAULT '',
        local_port                          VARCHAR(20) DEFAULT '80',
        protocol                            VARCHAR(150) DEFAULT '',
        remote_addr                         VARCHAR(45) DEFAULT '0.0.0.0',
        remote_host                         VARCHAR(150) DEFAULT '',
        remote_port                         VARCHAR(20) DEFAULT '0',
        scheme                              VARCHAR(100) DEFAULT 'http',
        server_name                         VARCHAR(250) DEFAULT '',
        server_port                         VARCHAR(20) DEFAULT '0',
        request_params                      TEXT,
        register                            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY(id)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
  ```
  
- COLUMN 정보
  - TxGatewayLog
  - HttpServletRequest 기준으로 테이블을 작성하였습니다. ([HttpServletRequest](https://tomcat.apache.org/tomcat-8.5-doc/servletapi/javax/servlet/http/HttpServletRequest.html))

    | column                 | comment                                                      |
    | ---------------------- | ------------------------------------------------------------ |
    | method                 | HTTP method with which this request was made, for example, GET, POST, or PUT. |
    | query_string           | query string that is contained in the request URL after the path. |
    | request_uri            | request's URL from the protocol name up to the query string in the first line of the HTTP request |
    | servlet_path           | request's URL that calls the servlet.                        |
    | path_info              | any extra path information associated with the URL the client sent when it made this request |
    | header_host            | header host                                                  |
    | header_user_agent      | header user agent info                                       |
    | header_accept          | header accept                                                |
    | header_accept_language | header accept language                                       |
    | header_accept_encoding | header accept encoding                                       |
    | header_referer         | header referer                                               |
    | header_rpccaller       | header rpc caller                                            |
    | header_origin          | header origin                                                |
    | header_connection      | header connection                                            |
    | header_content_length  | header content length                                        |
    | character encoding     | character encoding used in the body of this request          |
    | content_length         | length, in bytes, of the request body and made available by the input stream, or -1 if the length is not known |
    | content_type           | MIME type of the body of the request, or `null` if the type is not known |
    | local_addr             | Internet Protocol (IP) address of the interface on which the request was received |
    | locale                 | preferred `Locale` that the client will accept content in, based on the Accept-Language header |
    | local_name             | host name of the Internet Protocol (IP) interface on which the request was received |
    | local_port             | Internet Protocol (IP) port number of the interface on which the request was received |
    | protocol               | name and version of the protocol the request uses in the form *protocol/majorVersion.minorVersion*, for example, HTTP/1.1. |
    | remote_addr            | Internet Protocol (IP) address of the client or last proxy that sent the request. |
    | remote_host            | fully qualified name of the client or the last proxy that sent the request |
    | remote_port            | Internet Protocol (IP) source port of the client or last proxy that sent the request |
    | scheme                 | name of the scheme used to make this request, for example, `http`, `https`, or `ftp`. |
    | server_name            | host name of the server to which the request was sent        |
    | server_port            | port number to which the request was sent                    |
    | request_params         | request parameters                                           |
    | register               | register date                                                |
    
    
---

### CONFIGURATION
- TxGatewayAsyncConfig
  - 리퀘스트 정보를 DB 저장시 사용되는 @Async 설정 정보
  - ThreadPoolTaskExecutor 옵션 정보는 개발 및 배포 환경에 맞추어서 설정하시면 됩니다.
    ```java
    @Configurable
    @EnableAsync
    public class TxGatewayAsyncConfig implements AsyncConfigurer {
        /**
         * Executor Pool Size
         * @number
         */
        @Value("${txgw.router.async.poolSize:10}")
        private int poolSize;
    
        /**
         * Executor Max Pool Size
         * @number
         */
        @Value("${txgw.router.async.maxPoolSize:10}")
        private int maxPoolSize;
    
        /**
         * Executor Queue Capacity
         * @number
         */
        @Value("${txgw.router.async.queueCapacity:10}")
        private int queueCapacity;
    
        /**
         * Executor Keep Alive Seconds
         * @number
         */
        @Value("${txgw.router.async.keepAliveSeconds:10}")
        private int keepAliveSeconds;
    
        /**
         * TX GATEWAY Async initialize
         *
         * @return
         */
        @Bean(name="txGatewayAsyncExecutor")
        @Override
        public Executor getAsyncExecutor() {
            ThreadPoolTaskExecutor txGatewayAsyncExecutor = new ThreadPoolTaskExecutor();
            txGatewayAsyncExecutor.setCorePoolSize(poolSize);
            txGatewayAsyncExecutor.setMaxPoolSize(maxPoolSize);
            txGatewayAsyncExecutor.setQueueCapacity(queueCapacity);
            txGatewayAsyncExecutor.setKeepAliveSeconds(keepAliveSeconds);
            txGatewayAsyncExecutor.setAllowCoreThreadTimeOut(true);
            txGatewayAsyncExecutor.setWaitForTasksToCompleteOnShutdown(true);
            txGatewayAsyncExecutor.setThreadNamePrefix("TXGATEWAY-ASYNC-");
            txGatewayAsyncExecutor.initialize();
            return txGatewayAsyncExecutor;
        }
    
        @Override
        public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
            return new TxGatewayAsyncExceptionHandler();
        }
    }
    ```
  - properties 설정
    ```json
    # TXGW Async Configure
    txgw.router.async.poolSize=100
    txgw.router.async.maxPoolSize=100
    txgw.router.async.queueCapacity=50
    txgw.router.async.keepAliveSeconds=30
    ```

---

### SCHEDULER
- EP-QUEUE 모듈을 사용하여 실시간으로 요청되는 리퀘스트 로그를 Queue에 담아놓고 매 5초마다 Queue Count가 50개가 넘으면 Queue 전체 정보를 DB에 저장하도록 되어있습니다. 각각의 설정 정보는 개발 및 배포 환경에 따라 직접 수정하면 됩니다.

---

### QUERY MAPPER
- TxGateWay QUERY XML
  - namespace : com.klaytn.enterpriseproxy.mapper.txgw
  - resources/mapper/txgw.xml