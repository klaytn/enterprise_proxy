# Enterprise Proxy (Restful API)
EP 모듈을 사용하여 API 백엔드 서버를 구축함으로써 Klaytn 플랫폼의 강력하고 유용한 기능등을 HTTP API로 사용 가능하며 최종 결과물이 WAR로 컴파일 되어 WAS와 같이 배포,운영,관리가 가능합니다.

기본적으로 적용되어있는 Caver-Java,WEB3J,EP 모듈등을 이용하여 다양한 기능과 확장 가능한 구조로 좀더 쉽게 개발,구축할수 있습니다.

지원되는 프로토콜은 계속해서 개발할 예정입니다.



### CONFIGURATION
- Maven 3.5+
- Tomcat 8.5
- Jdk 1.8
- SpringBoot 2.1.3 Release
- JDBC
  - Mariadb 2.3.0
  - h2database 1.4.199
    - H2를 사용하는 경우 기능상 문제는 없으나 서버 확장에 대한 데이터 sync는 보장하지 않습니다.
    - 로컬 DB로 인해 제약이 많을수 있으므로 테스트 용도로 이용하기를 권장합니다.
  - Commons-dbcp 1.4
    - HikariCP는 지원하지 않습니다.
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
  - Mariadb,H2 지원하고 있습니다.
  - EP-TXGW 모듈에서 참조 하므로 Bean name을 수정하면 안됩니다.
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
  - Mariadb,H2 지원하고 있습니다.
  - EP-ROUTER 모듈에서 참조 하므로 Bean name을 수정하면 안됩니다.
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
  - 정보 수정시 코드 의존성을 고려해야 합니다.
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

### GETTING START
- maven compile
  - -P 옵션을 사용하여 DB properties (h2/maria) 중 선택, 컴파일 하도록 합니다.
  - EP-API는 배포를 위해 WAR파일로 컴파일됩니다.
  ```shell
  mvn clean package -P maria
  cd epi-api/src/target
  ep-api-0.0.1-SNAPSHOT.war 파일생성 확인
  ```
- application.properties
  ```shell
  klaytn.rpc.server.host=<EN NODE RPC HOST>
  klaytn.rpc.server.port=<EN NODE RPC PORT>
  * API에서 tagetHost 정보를 넣지 않으면 등록한 호스트로 접속합니다.
  ```
  ```shell
  logging.level.root=log level (debug,info,error...)
  * 현재 info 레벨이 적용되어있습니다.
  * 레벨은 개발 및 배포 환경에 따라 수정하면 됩니다.
  ```
  ```shell
  feepayer.address=ENC(...)
  feepayer.password=ENC(...)
  feepayer.keyStoreFilePath=ENC(...)
  * 대납 기능을 사용하기 위해 대납용 계정을 꼭 먼저 생성해주어야 합니다.
  * 대납 계정 정보는 최소한의 보안을 위해 Jasypt 암호화된 정보를 사용하도록 되어있습니다.
  ```
  ```shell
    ## Jasypt를 사용한 암호화 방법 ##
    1. http://www.jasypt.org/download.html
    2. unzip jasypt-1.9.2-dist.zip
    3. cd bin
    4. encrypt input="<암호할대상>" password="<복호화키 : 참고로 EP 소스에서는 '@klaytnep@'로 설정되어있습니다.>" algorithm="PBEWithMD5AndDES"
    5. RESULT
    ----ARGUMENTS-------------------
    algorithm: PBEWITHMD5ANDDES
    input: dbpassword
    password: encryptkey
    ----OUTPUT----------------------
    +VqidblzVqZJAGypmX65789787QrV0
  ```
- application-h2.properties
  - H2 DB 생성 파일 경로를 변경하고 싶은 경우 ***./h2db/*** 수정해주시면 됩니다.
  - Table Schema는 ***resources/sql/h2/*** 디렉토리에서 sql 파일을 확인하시면 됩니다.
  ```json
  eplog.db.datasource.url=jdbc:h2:file:./h2db/eplog;MODE=MySQL;INIT=CREATE SCHEMA IF NOT EXISTS eplog\\;RUNSCRIPT FROM 'classpath:sql/h2/txgateway.sql';AUTO_SERVER=TRUE;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=1;IGNORE_UNKNOWN_SETTINGS=TRUE
  ```
- application-maria.properties
  - 개발 및 배포 환경에 맞추어 설정하면 됩니다.
  - Table Schema는 ***resources/sql/maria*** 디렉토리에서 sql 파일을 확인하시면 됩니다.
  - DB 설정 및 테이블 생성을 먼저 진행하기를 권장합니다.
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
  - tomcat 설정은 개발 및 배포 환경에 따라 수정하면 됩니다.
  ```shell
  wget http://mirror.apache-kr.org/tomcat/tomcat-8/v8.5.41/bin/apache-tomcat-8.5.41.tar.gz
  tar xvfz apache-tomcat-8.5.41.tar.gz
  mv ep-api-0.0.1-SNAPSHOT.war mv apache-tomcat-8.5.41/webapps/ROOT.war
  cd apache-tomcat-8.5.41/bin
  ./startup.sh
  ```
- EP API SERVER 확인
  ```http
  http(s)://<EP_HOST>/swagger-ui.html
  ```

---

### SUPPORT API LIST
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
  - DB 데이터 운영을 위한 임시 API 입니다.
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
  - DB 데이터를 확인하기 위한 임시 API 입니다.
  - All Txgw Log List

---

### API CALL
- targetHost 설정
  - serviceChainId,serviceChainHostId 파라미터를 사용시 매칭되는 TargetHost로 접속하게 됩니다. (EP-ROUTER)
    ```http
    http://<EP_HOST>/management/admin/datadir?serviceChainId=1&serviceChainHostId=1
    ```
  - 직접 원하는 호스트를 지정하는 경우 targetHost 파라미터를 사용하면 됩니다.
    ```http
    http://<EP_HOST>/management/admin/datadir?targetHost=http://localhost:8551
    ```
  - 파라미터를 사용하지 않는 경우 properties에 설정된 호스트 정보로 접속하게 됩니다.
- Management / Admin / datadir 호출 예시
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
- 접속한 노드에서 지원하지 않는 RPC인 경우 **DISABLE API INTERFACE** 결과 리턴
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
- FeeDelegated / TxTypeFeeDelegatedValueTransfer 예시
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
  * 대납 계정 정보가 잘못된 경우 아래와 같이 리턴하도록 되어있습니다.
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
  * target 종류는 "api,rpc,transaction,router,fee delegated"가 있습니다.
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
  - ServiceChainHost 테이블에 등록된 호스트 정보 기준으로 매 3초마다 HealthCheck를 하도록 되어있습니다.
  - 해당 기능을 사용하지 않을 경우 ***init,destory*** 메소드를 제거 하시면 됩니다.
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
  - 리퀘스트정보를 Queue 저장후 매 5초마다 DB에 저장하도록 되어있습니다.
  - 해당 기능을 사용하지 않을 경우 ***init,destroy*** 메소드를 제거 하시면 됩니다.
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