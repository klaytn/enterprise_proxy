# Enterprise Proxy (Restful API)
By building backend server using EP module, you can use powerful and useful features of Kalytn platform as HTTP APIs. In addition, the deliverables are complied as WAR files so you can deploy, operate, and maintain them like WAS.

Caver-Java,WEB3J,EP modules, which are embedded by default, allow you to develop and build a system with various functions and scalability.

The supported modules will be updated gradually via user’s live feedback. Please send us many bug reports, feature requests and other opinions.



### CONFIGURATION
- Maven 3.5+
- Tomcat 8.5
- Jdk 1.8
- SpringBoot 2.1.3 Release
- JDBC
  - Mariadb 2.3.0
  - h2database 1.4.199
    - When using H2, there are no functional issues, but data sync for server expansion is not guaranteed.
    - We recommend to use it for only testing purposes, as there can be some limitations by local DB.
  - Commons-dbcp 1.4
    - HikariCP is not supported.
- Swagger 2.9.2
  ```java
  @Configuration
  @EnableSwagger2
  public class SwaggerConfig {
      @Value("${swagger.api.title:'EP RESTFUL API'}")
      private String title;
  
      @Value("${swagger.api.controller.basepackage:'com.klaytn.enterpriseproxy'}")
      private String basePackage;
  
      @Bean
      public Docket swaggerAPI() {
          return new Docket(DocumentationType.SWAGGER_2)
                  .select()
                  .apis(RequestHandlerSelectors.basePackage(basePackage))
                  .paths(PathSelectors.any())
                  .build()
                  .apiInfo(metaData());
      }
  
      private ApiInfo metaData() {
          return new ApiInfoBuilder().title(title).build();
      }
  }
  ```
- net.sf.ehcache 2.9.0
  ```java
  @Configuration
  @EnableCaching
  public class EhCacheConfig {
      @Bean
      public CacheManager cacheManager() {
          return new EhCacheCacheManager(Objects.requireNonNull(ehCacheManagerFactory().getObject()));
      }
  
      @Bean
      public EhCacheManagerFactoryBean ehCacheManagerFactory(){
          EhCacheManagerFactoryBean ehCacheBean = new EhCacheManagerFactoryBean();
          ehCacheBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
          ehCacheBean.setShared(true);
          return ehCacheBean;
      }
  }
  ```
  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:noNameSpaceSchemaLocation="ehcache.xsd"
           updateCheck="false"
           monitoring="autodetect"
           dynamicConfig="true">
  
      <diskStore path="java.io.tmpdir" />
      <cache name="EPLOCALCACHE"
             maxElementsInMemory="50000"
             eternal="false"
             timeToIdleSeconds="180"
             timeToLiveSeconds="180"
             overflowToDisk="false"
             diskPersistent="false"
             diskSpoolBufferSizeMB="100"
             diskExpiryThreadIntervalSeconds="180"
             memoryStoreEvictionPolicy="LRU">
      </cache>
  </ehcache>
  ```
- LogDatabaseConfig
  - Mariadb and H2 are supported.
  - Bean name shouldn't be modified because it's referred by EP-TXGW module.
    ```java
    @Configuration
    public class LogDatabaseConfig {
        @Autowired
        private ApplicationContext applicationContext;
    
        @Bean(name="logDataSource")
        @ConfigurationProperties(value="eplog.db.datasource")
        public DataSource logDataSource() {
            return DataSourceBuilder.create().build();
        }
    
        @Bean(name="logSqlSessionFactory")
        public SqlSessionFactory sqlSessionFactory(@Qualifier("logDataSource") DataSource dataSource) throws Exception {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);
            sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
            sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath*:/mapper/**/*.xml"));
            return sqlSessionFactoryBean.getObject();
        }
    
        @Bean(name="logDataBaseSessionTemplate")
        public SqlSessionTemplate sqlSessionTemplate(@Qualifier("logSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }
    ```
- OpDatabaseConfig
  - Mariadb and H2 are supported.
  - Bean name shouldn't be modified because it's referred by EP-ROUTER.
    ```java
    @Configuration
    public class OpDatabaseConfig {
        @Autowired
        private ApplicationContext applicationContext;
    
        @Bean(name="opDataSource")
        @ConfigurationProperties(value="epop.db.datasource")
        public DataSource opDataSource() {
            return DataSourceBuilder.create().build();
        }
    
        @Bean(name="opSqlSessionFactory")
        public SqlSessionFactory sqlSessionFactory(@Qualifier("opDataSource") DataSource dataSource) throws Exception {
            final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);
            sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
            sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath*:/mapper/**/*.xml"));
            return sqlSessionFactoryBean.getObject();
        }
    
        @Bean(name="opDataBaseSessionTemplate")
        public SqlSessionTemplate sqlSessionTemplate(@Qualifier("opSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }
    ```
- InterceptorConfig
  ```java
  @Configuration
  public class InterceptorConfig implements WebMvcConfigurer {
      @Resource
      private ApiRequestInterceptor interceptor;
  
      /**
       * Add Interceptors
       *  - custom area
       *
       * @param registry
       */
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
          registry.addInterceptor(interceptor)
                  .addPathPatterns("/management/**")
                  .addPathPatterns("/platform/**")
                  .addPathPatterns("/transaction/**")
                  .addPathPatterns("/feeDelegated/**");
      }
  }
  ```
- application.properties
  - Please make sure to consider the code dependencies when modifying properties.
  ```json
  ## editable properties
  # jsonrpc host info
  klaytn.rpc.server.host=https://api.baobab.klaytn.net
  klaytn.rpc.server.port=8651
  
  # feepayer
  feepayer.address=ENC(<feepayer address encrypt string>)
  feepayer.password=ENC(<feepayer password encrypt string>)
  feepayer.keyStoreFilePath=ENC(<feepayer wallet key store file path>)
  
  # logging
  logging.level.root=info
  logging.pattern.console=[%d{yyyy-MM-dd HH:mm:ss}] [%-5p] %C{1}.%M[%L] %m%n
  
  ## do not edit properties
  # ehcache
  spring.cache.ehcache.config=classpath:ehcache.xml
  
  # swagger
  swagger.api.title=EP-API
  swagger.api.controller.basepackage=com.klaytn.enterpriseproxy
  
  # jasypt encryptor
  jasypt.encryptor.bean=propertiesStringEncryptor
  ```
- application-h2.properties
  ```json
  ## editable properties
  # EPLOG (Enterprise Proxy Log)
  eplog.db.datasource.initialize=true
  eplog.db.datasource.driver-class-name=org.h2.Driver
  eplog.db.datasource.url=jdbc:h2:file:./h2db/eplog;MODE=MySQL;INIT=CREATE SCHEMA IF NOT EXISTS eplog\\;RUNSCRIPT FROM 'classpath:sql/h2/txgateway.sql';AUTO_SERVER=TRUE;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=1;IGNORE_UNKNOWN_SETTINGS=TRUE
  eplog.db.datasource.username=sa
  eplog.db.datasource.password=
  
  # EPOP (Enterprise Proxy Operation)
  epop.db.datasource.initialize=true
  epop.db.datasource.driver-class-name=org.h2.Driver
  epop.db.datasource.url=jdbc:h2:file:./h2db/epop;MODE=MySQL;INIT=CREATE SCHEMA IF NOT EXISTS epop\\;RUNSCRIPT FROM 'classpath:sql/h2/servicechain.sql';AUTO_SERVER=TRUE;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=1;IGNORE_UNKNOWN_SETTINGS=TRUE
  epop.db.datasource.username=sa
  epop.db.datasource.password=
  
  ## do not edit properties
  # profiles active
  spring.profiles.active=h2
  ```
- application-maria.properties
  ```json
  ## editable properties ##
  # EPOP (Enterprise Proxy Operation) DB
  epop.db.datasource.driverClassName=org.mariadb.jdbc.Driver
  epop.db.datasource.url=jdbc:mariadb:<MARIA DB HOST>/epop
  epop.db.datasource.username=<ID>
  epop.db.datasource.password=<PASSWORD>
  epop.db.datasource.initialSize=10
  epop.db.datasource.maxWait=6000
  epop.db.datasource.maxActive=50
  epop.db.datasource.validationQuery=select 1
  epop.db.datasource.testOnBorrow=true
  epop.db.datasource.testOnReturn=false
  epop.db.datasource.testWhileIdle=true
  epop.db.datasource.maxIdle=30
  epop.db.datasource.minIdle=10
  epop.db.datasource.timeBetweenEvictionRunsMillis=5000
  
  # EPLOG (Enterprise Proxy Log) DB
  eplog.db.datasource.driverClassName=org.mariadb.jdbc.Driver
  eplog.db.datasource.url=jdbc:mariadb:<MARIA DB HOST>/eplog
  eplog.db.datasource.username=<ID>
  eplog.db.datasource.password=<PASSWORD>
  eplog.db.datasource.initialSize=10
  eplog.db.datasource.maxWait=6000
  eplog.db.datasource.maxActive=50
  eplog.db.datasource.validationQuery=select 1
  eplog.db.datasource.testOnBorrow=true
  eplog.db.datasource.testOnReturn=false
  eplog.db.datasource.testWhileIdle=true
  eplog.db.datasource.maxIdle=30
  eplog.db.datasource.minIdle=10
  eplog.db.datasource.timeBetweenEvictionRunsMillis=5000
  
  ## do not edit properties
  # profiles active
  spring.profiles.active=maria
  ```
- Mybatis 3.4.1
  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
  <configuration>
      <settings>
          <setting name="useGeneratedKeys" value="true"/>
          <setting name="callSettersOnNulls" value="true"/>
          <setting name="lazyLoadingEnabled" value="true"/>
          <setting name="aggressiveLazyLoading" value="true"/>
          <setting name="defaultExecutorType" value="SIMPLE"/>
          <setting name="defaultStatementTimeout" value="200"/>
      </settings>
      <typeHandlers>
          <typeHandler javaType="java.sql.Timestamp" handler="org.apache.ibatis.type.DateTypeHandler" />
          <typeHandler javaType="java.sql.Time" handler="org.apache.ibatis.type.DateTypeHandler" />
          <typeHandler javaType="java.sql.Date" handler="org.apache.ibatis.type.DateTypeHandler" />
      </typeHandlers>
  </configuration>
  ```

---

### GETTING STARTED
- maven compile
  - Use the -P option to select and compile DB properties h2 or maria.
  - The EP-API is compiled into a WAR file for distribution.
  ```shell
  mvn clean package -P maria
  cd epi-api/src/target
  * ep-api-0.0.1-SNAPSHOT.war file will be created.
  ```
- application.properties
  ```shell
  klaytn.rpc.server.host=<EN NODE RPC HOST>
  klaytn.rpc.server.port=<EN NODE RPC PORT>
  * The registered host is called by default if targetHost information is missing in the API.
  ```
  ```shell
  logging.level.root=log level (debug,info,error...)
  * The logging level is set as info by default.
  * You can change the level according to your dev or prod environment.
  ```
  ```shell
  feepayer.address=ENC(...)
  feepayer.password=ENC(...)
  feepayer.keyStoreFilePath=ENC(...)
  * You must create the delegated fee payer account in advance to use fee delegation functions.
  * The fee payer account information is designed to use Jasypt encryption for security reasons.
  ```
  ```shell
    ## How to use Jasypt to encrypt ##
    1. http://www.jasypt.org/download.html
    2. unzip jasypt-1.9.2-dist.zip
    3. cd bin
    4. encrypt input="<target object to encrypt>" password="<decryption key: FYI, it is set as '@kalynep@ in EP source.>" algorithm="PBEWithMD5AndDES"
    5. RESULT
    ----ARGUMENTS-------------------
    algorithm: PBEWITHMD5ANDDES
    input: dbpassword
    password: encryptkey
    ----OUTPUT----------------------
    +VqidblzVqZJAGypmX65789787QrV0
  ```
- application-h2.properties
  - You can modify ***./h2db/*** to change H2 DB file creation path.
  - You can check table schema from sql files in ***resources/sql/h2/***.
  ```json
  eplog.db.datasource.url=jdbc:h2:file:./h2db/eplog;MODE=MySQL;INIT=CREATE SCHEMA IF NOT EXISTS eplog\\;RUNSCRIPT FROM 'classpath:sql/h2/txgateway.sql';AUTO_SERVER=TRUE;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=1;IGNORE_UNKNOWN_SETTINGS=TRUE
  ```
- application-maria.properties
  - Set these according to your dev or prod environments.
  - You can check table schema from sql files in ***resources/sql/maria***.
  - We recommend to configure DB and create tables in advance.
  ```json
  epop.db.datasource.driverClassName=org.mariadb.jdbc.Driver
  epop.db.datasource.url=jdbc:mariadb:<MARIA DB HOST>/epop
  epop.db.datasource.username=<ID>
  epop.db.datasource.password=<PASSWORD>
  epop.db.datasource.initialSize=10
  epop.db.datasource.maxWait=6000
  epop.db.datasource.maxActive=50
  epop.db.datasource.validationQuery=select 1
  epop.db.datasource.testOnBorrow=true
  epop.db.datasource.testOnReturn=false
  epop.db.datasource.testWhileIdle=true
  epop.db.datasource.maxIdle=30
  epop.db.datasource.minIdle=10
  epop.db.datasource.timeBetweenEvictionRunsMillis=5000
  ```
- tomcat
  - Set tomcat settings according to your dev or prod environments.
  ```shell
  wget http://mirror.apache-kr.org/tomcat/tomcat-8/v8.5.41/bin/apache-tomcat-8.5.41.tar.gz
  tar xvfz apache-tomcat-8.5.41.tar.gz
  mv ep-api-0.0.1-SNAPSHOT.war mv apache-tomcat-8.5.41/webapps/ROOT.war
  cd apache-tomcat-8.5.41/bin
  ./startup.sh
  ```
- Verifying EP API SERVER
  ```http
  http(s)://<EP_HOST>/swagger-ui.html
  ```

---

### SUPPORTED API LIST
- **Klaytn RPC**
  - Management
    - admin
    - debug
    - personal
    - txpool
  - Platform
    - Klay
      - Account
      - Block
      - Transaction
      - Configuration
      - Filter
    - Net
- **Fee Delegated Transaction**
  - Account
    - TxTypeFeeDelegatedAccountUpdate
    - TxTypeFeeDelegatedAccountUpdateWithRatio
  - Cancel
    - TxTypeFeeDelegatedCancel
    - TxTypeFeeDelegatedCancelWithRatio
  - SmartContract
    - TxTypeFeeDelegatedSmartContractDeploy
    - TxTypeFeeDelegatedSmartContractDeployWithRatio
    - TxTypeFeeDelegatedSmartContractExecution
    - TxTypeFeeDelegatedSmartContractExecutionWithRatio
  - ValueTransfer
    - TxTypeFeeDelegatedValueTransfer
    - TxTypeFeeDelegatedValueTransferWithRatio
    - TxTypeFeeDelegatedValueTransferMemo
    - TxTypeFeeDelegatedValueTransferMemoWithRatio
- **Raw Transaction Send**
  - Signed RawTransaction Send
- **Operation**
  - These are temporary APIs for DB data operation works.
  - All Chain List
  - Service Chain
    - Add
    - Edit
    - Delete
  - Service Chain Host
    - Add
    - Edit
    - Delete
  - All Health Check Log
- **Txgw**
  - These are temporary APIs for DB data verification.
  - All Txgw Log List

---

### API CALL
- targetHost settings
  - Using serviceChainId and serviceChainHostId parameters allows you to connect to the matching TargetHost. (EP-ROUTER)
    ```http
    http://<EP_HOST>/management/admin/datadir?serviceChainId=1&serviceChainHostId=1
    ```
  - Using targetHost parameter directly allows you to connect to the specified host.
    ```http
    http://<EP_HOST>/management/admin/datadir?targetHost=http://localhost:8551
    ```
  - When no parameters are used, it connects to the host specified in properties by default.
- Examples of Management / Admin / datadir calls
  ```http
  http://<EP_HOST>/management/admin/datadir?serviceChainId=<ID>&serviceChainHostId=<ID>
  http://<EP_HOST>/management/admin/datadir?targetHost=<NODE HOST>
  ```
  ```json
  {
    "code": 0,
    "target": "api",
    "result": "SUCCESS",
    "data": "/data/kend/data"
  }
  ```
- **DISABLE API INTERFACE** is returned if the connected node doesn't support the RPC.
  ```json
  {
    "code": 799,
    "data": {
      "klay": "1.0",
      "net": "1.0",
      "rpc": "1.0"
    },
    "result": "DISABLE API INTERFACE",
    "target": "api"
  }
  ```
- Examples of FeeDelegated / TxTypeFeeDelegatedValueTransfer
  ```http
  http://<EP_HOST>/feeDelegated/valueTransfer/?serviceChainId=1&serviceChainHostId=1&chainId=<CHAINID>&rawTransaction=<TxTypeFeeDelegatedValueTransfer RAW TRANSACTION>
  ```
  ```json
  {
    "code": 0,
    "data": "<fee delgated transaction result receipt>",
    "result": "SUCCESS",
    "target": "api"
  }
  ```
  ```json
  * The followings are returned if the fee delegated account information is incorrect.
  {
    "code": 899,
    "data": "Invalid password provided",
    "result": "TRANSACTION ERROR",
    "target": "fee delegated"
  }
  ```

---

### API RESPONSE
- RESPONSE MODEL
  ```json
  {
    "code": 0,
    "data": {},
    "result": {},
    "target": "string"
  }
  * target can be one of these: api, rpc, transaction, router, fee delegated
  ```
- RESPONSE CODE
  ```java
  UNKNOWN(999,"UNKNOWN")
  PARAMETER_INVALID(998,"PARAMETER INVALID")
  EMPTY_DATA(997,"EMPTY DATA")
  RPC_CALL_ERROR(996,"RPC CALL ERROR")
  DB_ERROR(995,"DB EXECUTE ERROR")
  ...
  TX_ERROR(899,"TRANSACTION ERROR")
  ...
  DISABLE_APIS(799,"DISABLE API INTERFACE")
  ...
  SUCCESS(0,"SUCCESS")
  ```

---

### SCHEDULER
- api/common/router/RouterHandler.java
  - HealthCheck is scheduled to be performed every 3 seconds for the hosts registered in ServiceChainHost table.
  - If you don't want to use this function, please remove ***init, destroy*** methods.
  ```java
  @Component
  public class RouterHandler {
  		@Autowired
  		private ChainInfoServices services;
  
      @Autowired
      private HealthCheckScheduler scheduler;
    
  		....
        
  		/**
       * health checker scheduler start
       *
       * @PostConstruct
       */
      @PostConstruct
      public void init() {
          scheduler.start();
      }
  
      /**
       * health checker scheduler stop
       * 
       * @PreDestory
       */
      @PreDestroy
      public void destroy() {
          scheduler.stop();
      }
  }
  ```
- api/common/txgw/TxgwHandler.java
  - Requests are scheduled to be stored in DB every 5 seconds, after being stored in Queue.
  - If you don't want to use function, please remove ***init, destroy*** methods.
  ```java
  @Component
  public class TxgwHandler {
      @Autowired
      private TxgwService service;
      
      @Autowired
      private LocalQueueScheduler scheduler;
      
      ....
      
      /**
       * txgw scheduler start
       * 
       * @PostConstruct
       */
      @PostConstruct
      public void init() {
          scheduler.start();
      }
  
      /**
       * txgw scheduler stop
       *
       * @PreDestory
       */
      @PreDestroy
      public void destroy() {
          scheduler.stop();
      }
  }
  ```
