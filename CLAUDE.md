# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

METAMAC Statistical Resources is an enterprise Java application for managing statistical data resources (datasets, publications, queries, and multidatasets). It's a multi-module Maven project with REST APIs, a GWT-based administrative UI, event-driven Kafka integration, and PostgreSQL persistence.

**Main branch for PRs**: `develop`

## Build & Development Commands

### Building the Project

```bash
# Build entire project (all modules)
mvn clean install

# Build without tests
mvn clean install -DskipTests

# Build specific module
cd metamac-statistical-resources-core
mvn clean install

# Package WAR files for deployment
mvn clean package
```

### Running Tests

```bash
# Run all tests
mvn test

# Run tests for specific module
cd metamac-statistical-resources-core
mvn test

# Run single test class
mvn test -Dtest=DatasetVersionRepositoryTest

# Run specific test method
mvn test -Dtest=DatasetVersionRepositoryTest#testFindByUrn
```

### GWT Development (Web UI)

```bash
# Compile GWT application
cd metamac-statistical-resources-web
mvn gwt:compile

# Run in GWT development mode (requires proper configuration)
mvn gwt:run
```

### Code Generation

The project uses Fornax Sculptor for generating domain model base classes and Avro for Kafka message schemas. These are generated during Maven build:

```bash
# Regenerate Avro schemas (Kafka module)
cd metamac-statistical-resources-kafka
mvn clean generate-sources

# Regenerate JAXB classes from XSD schemas (REST API modules)
cd metamac-statistical-resources-rest-internal-api
mvn clean generate-sources
```

## Module Architecture

The project consists of 12 modules organized by layer and responsibility:

### Core Layer
- **metamac-statistical-resources-parent**: Parent POM with shared dependencies and versions
- **metamac-statistical-resources-kafka**: Kafka Avro schemas (20 schemas) for event publishing
- **metamac-statistical-resources-core**: Business logic, domain entities, repositories, services

### REST API Layer
- **metamac-statistical-resources-rest-internal-api**: Internal API contracts (XSD schemas)
- **metamac-statistical-resources-rest-internal-impl**: Internal API implementation (administrative)
- **metamac-statistical-resources-rest-external-api**: External API contracts (XSD schemas)
- **metamac-statistical-resources-rest-external-impl**: External API implementation (public read-only)
- **metamac-sdmx-data-rest-external-impl**: SDMX 2.1 format API implementation
- **metamac-statistical-resources-rest-common-impl**: Shared REST utilities and mappers
- **metamac-statistical-resources-common-impl**: Common implementation utilities

### Web UI Layer
- **metamac-statistical-resources-web**: GWT-based administrative web application (WAR)
- **metamac-statistical-resources-external-web**: Public-facing web application (WAR)

### Dependency Flow
```
Core (domain + services)
  ↓
REST API Implementation (facades + mappers)
  ↓
Web Applications (GWT UI + REST consumers)

Kafka (parallel integration for events)
```

## Technology Stack

### Backend
- **Framework**: Spring 3.0 (XML configuration)
- **ORM**: Hibernate + JPA annotations
- **Database**: PostgreSQL with manual SQL migration scripts
- **Code Generation**: Fornax Sculptor (domain model), JAXB2 (REST APIs), Avro (Kafka)
- **REST**: Apache CXF with JAX-RS
- **Messaging**: Apache Kafka with Avro serialization

### Frontend
- **Framework**: Google Web Toolkit (GWT) 2.5+
- **UI Components**: SmartGWT
- **Architecture**: MVP pattern via GWT Platform (GWTP)
- **Dependency Injection**: Google GIN (GWT Injection)

### Testing
- **Unit**: JUnit 4.x
- **Mocking**: Mockito
- **Database**: DBUnit for fixtures
- **Integration**: Spring Test framework

## Domain Model

The core domain follows an inheritance hierarchy:

```
IdentifiableStatisticalResource
  └── VersionableStatisticalResource
      ├── SiemacMetadataStatisticalResource (base metadata)
      │   ├── Dataset
      │   ├── Publication
      │   ├── Query
      │   └── Multidataset
      │
      └── LifeCycleStatisticalResource (versioned entities with lifecycle)
          ├── DatasetVersion (TB_DATASETS_VERSIONS)
          ├── PublicationVersion (TB_PUBLICATIONS_VERSIONS)
          ├── QueryVersion (TB_QUERIES_VERSIONS)
          └── MultidatasetVersion (TB_MULTIDATASETS_VERSIONS)
```

### Key Domain Patterns
- **Versioning**: Resources have parent entities (Dataset) and versioned entities (DatasetVersion)
- **Lifecycle States**: DRAFT → VALIDATION_REQUESTED → VALIDATED → PUBLISHED
- **Internationalization**: InternationalString entity with Translation entries for multi-language support (es, ca, en, pt)
- **External References**: ExternalItem for linking to SRM, Statistical Operations, Common Metadata
- **Code Generation**: Base classes (*Base.java) are auto-generated; extend them in src/main/java

## Database Management

### Schema Location
- Creation scripts: `etc/db/statistical-resources/postgresql/01-create/`
- Drop scripts: `etc/db/statistical-resources/postgresql/02-drop/`

### Migration Process
1. Database changes are applied manually via SQL scripts
2. Scripts are versioned in `etc/changes-from-release/{version}/db/`
3. Follow the upgrade path in `UPGRADE.md` when moving between versions
4. Each migration may include schema changes, data transformations, and Kafka topic resets

### Key Tables
- `TB_DATASETS`, `TB_DATASETS_VERSIONS`
- `TB_PUBLICATIONS`, `TB_PUBLICATIONS_VERSIONS`
- `TB_QUERIES`, `TB_QUERIES_VERSIONS`
- `TB_MULTIDATASETS`, `TB_MULTIDATASETS_VERSIONS`
- `TB_INTERNATIONAL_STRINGS`, `TB_TRANSLATIONS`
- `TB_EXTERNAL_ITEMS`, `TB_CATEGORISATIONS`
- `TB_GEOCOV_VARELEM_CACHE_DATASETS_VERSIONS` (performance cache)

### Sequences
All primary keys use PostgreSQL sequences:
- `SEQ_DATASETS`, `SEQ_DATASETS_VERSIONS`
- `SEQ_PUBLICATIONS`, `SEQ_PUBLICATIONS_VERSIONS`
- `SEQ_QUERIES`, `SEQ_QUERIES_VERSIONS`
- `SEQ_I18NSTRS`, `SEQ_EXTERNAL_ITEMS`

## REST API Design

### Internal API (Administrative)
- **Base Path**: `/statistical-resources/v1.0/`
- **Purpose**: Full CRUD operations, lifecycle management, administrative tasks
- **Facade**: `StatisticalResourcesRestInternalFacadeV10Impl`
- **Authentication**: Required via METAMAC SSO

### External API (Public)
- **Base Path**: `/statistical-resources-external/v1.0/`
- **Purpose**: Read-only access to published resources
- **Facade**: `StatisticalResourcesRestExternalFacadeV10Impl`
- **Authentication**: Public (no auth required)

### SDMX API
- **Base Path**: `/sdmx/data/v2.1/`
- **Purpose**: SDMX 2.1 format data export
- **Facade**: `SdmxDataRestExternalFacadeV21Impl`

### API Patterns
- JAXB-based: XSD schemas define contracts, JAXB2 plugin generates Java classes
- Field projection: `?fields=+id,+name,-metadata` to control response size
- Mappers: Domain → Do (internal) → REST DTO → JSON/XML
- Versioned endpoints: Multiple API versions can coexist

## Kafka Integration

### Avro Schemas
Located in `metamac-statistical-resources-kafka/src/main/resources/avro/`:
- Core schemas: `datetime.avsc`, `international-string.avsc`, `external-item.avsc`
- Domain schemas: `dataset-version.avsc`, `publication-version.avsc`, `query-version.avsc`
- Structural schemas: `code-dimension.avsc`, `dimension-representation-mapping.avsc`

### Event Publishing
- **Service**: `StreamMessagingService<K, V>`
- **Topics**: `DATASET_PUBLICATIONS`, `COLLECTION_PUBLICATIONS`, `QUERY_PUBLICATIONS`, `JAXI_PUBLICATIONS`
- **Schema Registry**: Confluent Schema Registry (default: http://localhost:8081)
- **Trigger Points**: Resource lifecycle transitions (publish, version, update)

### Kafka Commands (from UPGRADE.md)
```bash
# Delete schema from registry
curl -X DELETE http://localhost:8081/subjects/DATASET_PUBLICATIONS-value

# Clear topic messages (set retention to 100ms, wait, restore)
/servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 \
  --entity-type topics --entity-name DATASET_PUBLICATIONS \
  --add-config retention.ms=100 --alter

# After 1 minute, restore retention
/servers/kafka/confluent/bin/kafka-configs --bootstrap-server localhost:19092 \
  --entity-type topics --entity-name DATASET_PUBLICATIONS \
  --delete-config retention.ms --alter
```

## Configuration

### Spring Configuration
- **Core**: `metamac-statistical-resources-core/src/main/resources/spring/statistical-resources/applicationContext.xml`
- **REST Internal**: `metamac-statistical-resources-rest-internal-impl/src/main/resources/spring/statistical-resources-rest-internal/applicationContext.xml`
- **REST External**: `metamac-statistical-resources-rest-external-impl/src/main/resources/spring/statistical-resources-rest-external/applicationContext.xml`

### Environment Configuration
- **Location**: `classpath:metamac/environment.xml` (can override via `$STATISTICAL_RESOURCES_CONF_PATH`)
- **Contains**:
  - Database connection settings
  - External service endpoints (SRM, Statistical Operations, Common Metadata)
  - Kafka broker configuration
  - File upload paths
  - Feature flags (e.g., Twitter posting enabled)

### External Service Dependencies
The application integrates with:
- **SRM** (Structural Resources Manager): Code lists, classifications, variables
- **Statistical Operations**: Operations metadata and definitions
- **Common Metadata**: Shared metadata definitions
- **Notifications**: Event notification system
- **Dataset Repository**: Separate persistence layer for data (not metadata)

## Important Development Patterns

### Generated vs Manual Code
- **Generated**: `src/generated/java` - Do not modify! Regenerated on build
- **Manual**: `src/main/java` - Extend generated base classes here
- **Example**: `DatasetVersionBase` (generated) → `DatasetVersion` (manual extensions)

### Service Layer Pattern
- Services are Spring beans with `@Service` annotation
- Implement business logic and orchestration
- Call repositories for persistence
- Publish events to Kafka after state transitions
- Validate inputs using validator classes

### Mapper Pattern
Three-tier mapping:
1. **Domain entities** (JPA entities in core)
2. **Do objects** (internal data transfer objects)
3. **REST DTOs** (JAXB-generated from XSD)

Mappers: `*Do2RestMapper`, `*Rest2DoMapper`, `*Dto2DoMapper`, `*Do2DtoMapper`

### Testing Pattern
- Base test classes: `*BaseTest` with common setup
- DBUnit fixtures: `src/test/resources/dbunit/` (XML files)
- Test Spring context: `applicationContext-test.xml`
- Transaction rollback: Tests run in transactions, rolled back after

### Internationalization
All user-facing text uses `InternationalString`:
```java
InternationalString title = new InternationalString();
title.addLocalizedString(new LocalizedString("es", "Título en español"));
title.addLocalizedString(new LocalizedString("en", "Title in English"));
```

## Code Locations

### Finding Key Components
- **Domain entities**: `metamac-statistical-resources-core/src/main/java/org/siemac/metamac/statistical/resources/core/dataset/domain/`
- **Services**: `metamac-statistical-resources-core/src/main/java/org/siemac/metamac/statistical/resources/core/lifecycle/serviceimpl/`
- **REST endpoints**: `metamac-statistical-resources-rest-*-impl/src/main/java/org/siemac/metamac/statistical_resources/rest/**/v1_0/service/`
- **GWT UI**: `metamac-statistical-resources-web/src/main/java/org/siemac/metamac/statistical/resources/web/client/`
- **Kafka Avro schemas**: `metamac-statistical-resources-kafka/src/main/resources/avro/`
- **Tests**: `*/src/test/java/` (mirrors main source structure)

### Common Files to Edit
- **Adding new lifecycle service**: `metamac-statistical-resources-core/src/main/java/org/siemac/metamac/statistical/resources/core/lifecycle/serviceimpl/LifecycleServiceImpl.java`
- **Adding REST endpoint**: REST impl modules under `service/` package
- **Updating domain model**: Sculptor DSL files (if present) or extend generated entities
- **Adding Kafka event**: Create new .avsc file in kafka module, regenerate sources

## Docker and Deployment

### Docker Configuration
- **Location**: `dockerfiles/` and `etc/docker/`
- **Server Config**: `dockerfiles/server.xml` (Tomcat configuration)
- **Kafka Environment**: Hosted in parent project `metamac-parent/etc/docker`

### Build Artifacts
- **WARs**: `metamac-statistical-resources-web/target/*.war`, `metamac-statistical-resources-external-web/target/*.war`
- **JARs**: All other modules produce JARs for library reuse

## Common Development Workflows

### Adding a New Field to DatasetVersion
1. Update database: Create SQL script in `etc/changes-from-release/`
2. Update entity: Extend `DatasetVersion` class (if not in generated base)
3. Update Avro schema: Modify `dataset-version.avsc` and regenerate
4. Update mappers: Add field to `*Do2RestMapper`, `*Rest2DoMapper`
5. Update REST API: Modify XSD if needed, regenerate JAXB classes
6. Add tests: Update DBUnit fixtures and add test cases
7. Update Kafka schema registry: Delete old schema, republish

### Publishing a Dataset
Lifecycle: DRAFT → VALIDATION_REQUESTED → VALIDATED → PUBLISHED

Code locations:
- `LifecycleServiceImpl.sendDatasetVersionToPublished()`
- Publishes Kafka event via `StreamMessagingService`
- Updates `TB_DATASETS_VERSIONS.PROC_STATUS`

### Updating External Service Integration
External service clients in `metamac-statistical-resources-core`:
- `SrmRestInternalService`: SRM integration
- `StatisticalOperationsRestInternalService`: Operations metadata
- `CommonMetadataRestExternalService`: Common metadata

Update endpoint configuration in `environment.xml`

### Code formatting:
Use the Eclipse formatter defined in the following path: C:\Users\arte40\Documents\WorkSpaceArte\arte-arte-formatter_con_align_fields.xml

### Nomenclatura y Código

- **Idioma del código**: SIEMPRE en inglés
  - Clases, métodos, variables, constantes, comentarios
  - Ejemplo: `UserService`, `findActiveUsers()`, `MAX_LOGIN_ATTEMPTS`

- **CamelCase**: Para variables y métodos
  ```java
  String userName;
  boolean isActiveUser;
  void assignRoleToUser(Long userId, Long roleId);
  ```
 ### Restricciones Java 7

Este es un proyecto **legacy** - mantener compatibilidad Java 7:

#### ❌ NO Usar (Java 8+)

```java
// ❌ Lambdas
users.forEach(u -> LOG.info(u.getName()));

// ❌ Streams
List<String> names = users.stream()
    .map(User::getName)
    .collect(Collectors.toList());

// ❌ Optional
Optional<User> user = userRepository.findById(id);

// ❌ LocalDate/LocalDateTime
LocalDate now = LocalDate.now();

// ❌ Method references
users.sort(Comparator.comparing(User::getName));
```

#### ✅ Usar (Java 7)

```java
// ✅ Bucles tradicionales
for (User user : users) {
    LOG.info(user.getName());
}

// ✅ Iteración manual para mapeo
List<String> names = new ArrayList<String>();
for (User user : users) {
    names.add(user.getName());
}

// ✅ Null checks tradicionales
User user = userRepository.findById(id);
if (user != null) {
    // ...
}

// ✅ Joda Time
DateTime now = new DateTime();
DateTime tomorrow = now.plusDays(1);

// ✅ Comparadores anónimos
Collections.sort(users, new Comparator<User>() {
    @Override
    public int compare(User u1, User u2) {
        return u1.getName().compareTo(u2.getName());
    }
});
```

#### Inferencia de Tipos en Genéricos

**Depende del módulo**:

```java
// Frontend (GWT) - NO PERMITIDA inferencia (limitación GWT 2.3.0)
Map<String, String> map = new LinkedHashMap<String, String>();
List<UserDto> users = new ArrayList<UserDto>();

// Backend/REST - SÍ PERMITIDA inferencia (Java 7)
Map<String, String> map = new LinkedHashMap<>();
List<UserDto> users = new ArrayList<>();
```

### Joda Time (Fechas)

**NO usar** `java.time.*` (Java 8+)

```java
// ✅ Usar Joda Time
import org.joda.time.DateTime;

DateTime createdDate = new DateTime();
DateTime yesterday = createdDate.minusDays(1);
DateTime nextWeek = createdDate.plusWeeks(1);

boolean isBefore = createdDate.isBefore(yesterday);

// Formateo
DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
String formatted = createdDate.toString(fmt);

// Persistencia JPA/Hibernate
@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
@Column(name = "CREATED_DATE")
private DateTime createdDate;
```

## Seguridad

### Autenticación

- **CAS 5.3.12.1**: Central Authentication Service
- **METAMAC SSO 4.11.2**: Single Sign-On personalizado
- Login centralizado para todas las aplicaciones METAMAC

### Autorización

**3 niveles de validación**:

1. **Frontend (Web)**:
   - Ocultar/deshabilitar acciones según permisos
   - Mejora UX pero NO es seguridad real

2. **Backend (Core)**:
   - Validación REAL antes de ejecutar operaciones
   - `@PreAuthorize`, custom interceptors

3. **API REST**:
   - Endpoint `/permissions/check` para otros sistemas
   - Cache agresivo para performance