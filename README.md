<style>
.custom-image {
  width: 250px;
  height: auto;
  margin: 10px
}
</style>

# My Signals project
Aboubacar BAH\
Taoufiq Boussraf

## Objectives

This project involves collecting health data in real time from a "MySignals" IoT device to which several sensors are connected. Once collected, this data needs to be stored in a personal cloud API to be visualised in the form of graphs.

![alt text for screen readers](https://github.com/kindy235/mysignals/blob/main/images/architecture.png?raw=true "MySignals device and Sensors")

**The main objectives are :** 

1. **Real-Time Data Collection:** Implement a solution to collect real-time data from health sensors connected to the MySignals IoT device.

2. **Integration of Multiple Sensors:** Enable the connection and integration of various types of health sensors such as ECG, EEG, temperature, blood pressure, glucose level, etc.

3. **Storage in Personal Cloud:** Develop a cloud API to securely store and manage collected data in a scalable manner.

4. **Data Visualization:** Create visualization features for data in the form of graphs and tables, allowing users to monitor and analyze collected health data.

5. **User-Friendly Interface:** Design a user-friendly interface that enables users to log in, view their data, and add new sensors data to the system.

6. **Data Security:** Implement security measures to protect sensitive health data stored in the personal cloud, using robust encryption and authentication methods.

7. **Multi-Platform Compatibility:** Ensure compatibility with different platforms, including mobile devices and computers, allowing users to access data from any device.

8. **Notifications and Alerts:** Integrate notification and alert features to inform users of abnormal fluctuations or critical situations in health data.

## Specifications

![alt text for screen readers](https://github.com/kindy235/mysignals/blob/main/images/mysignals-sensors.png?raw=true "MySignals device and Sensors")


1. **MySignals IoT Configuration:** Configure and set up the MySignals IoT device by connecting appropriate sensors and configuring communication settings.

2. **Cloud API Development:** Design and establish a secure cloud API for storing and managing collected health data.

3. **Android App Development:** Create an attractive and intuitive user interface that allows users to log in, connecting to MySignals Device, view health data, and collect sensors data from the device.

4. **Data Visualization:** Develop interactive graphs and charts for visualizing health data provided by The Cloud API (By using an android App or a Web Application).

5. **Cloud Connectivity Integration:** Implement bidirectional communication between the MySignals device and the cloud API for real-time data transmission.

6. **Security and Authentication:** Implement security measures, including data encryption and authentication mechanisms, to ensure data confidentiality and integrity.

7. **Notifications and Alerts:** Integrate notification and alert mechanisms to inform users of important events related to their health data.

8. **Testing and Validation:** Perform comprehensive testing to ensure that data collection, storage, visualization, and management functions correctly and securely.

9. **Documentation and Training:** Provide a documentation on how to use the application, along with user training on adding sensors, viewing data, and interacting with the user interface.

10. **Deployment and Maintenance:** Deploy the cloud application and ensure ongoing maintenance to ensure optimal performance and updates as needed.


## API Development

The API is based SpringBoot API

### Tools

- IntelliJ IDEA, Visual Studio Code...
- Java JDK & JRE (Version >= 8)
- Maven

### Architecture

![API Architecture](https://github.com/kindy235/mysignals/blob/main/images/architecture-api.png?raw=true "MySignals device and Sensors")

### Services

In the SpringBoot API, we have three services : 

#### Sensors Service
The Sensors Service is responsible for managing the data related to various sensors used in the system. It implements CRUD (Create, Read, Update, Delete) operations to handle sensor information. This service allows you to perform the following actions:

- **Create:** Add new sensor information to the database, including details like sensor type, data units, and any other relevant metadata.
- **Read:** Retrieve sensor information from the database based on different criteria, such as sensor type or sensor ID.
- **Update:** Modify existing sensor information, such as updating sensor metadata or changing sensor properties.
- **Delete:** Remove sensor information from the database when a sensor is no longer needed or is replaced.

The Sensors Service plays a crucial role in storing and managing the characteristics and properties of different sensors within the system.

#### Member Service
The Member Service handles the management of member data within the system. Similar to the Sensors Service, it provides CRUD operations for member-related information. This service allows you to perform the following actions:

- **Create:** Add new member profiles to the system, including details like name, surname, profile picture, description, height, weight, and other relevant attributes.
- **Read:** Retrieve member profiles from the database based on different criteria, such as member ID or specific attributes.
- **Update:** Modify existing member profiles, including updating personal information or adjusting attributes like height and weight.
- **Delete:** Remove member profiles from the system when necessary.

The Member Service is essential for maintaining records of individuals associated with the health data and providing the necessary details for data analysis and visualization.

#### Authentication Service (JWT Authentication)
The Authentication Service is responsible for handling user authentication and authorization using JSON Web Tokens (JWT). It provides the necessary functionality to securely manage user access to the API endpoints. This service includes the following features:

- **User Registration:** Allow users to create accounts by providing necessary details, such as username, email, and password.
- **User Login:** Authenticate users by verifying their credentials and generating JWT tokens upon successful login.
- **Token Validation:** Verify the authenticity and integrity of incoming JWT tokens to ensure that users have valid access.
- **Authorization:** Control user access to specific endpoints and resources based on their roles and permissions stored within the JWT.
- **Token Refresh:** Provide the ability to refresh expired tokens, enabling users to extend their session without the need for frequent login.

The Authentication Service ensures that only authorized users can access the API endpoints and perform actions, contributing to the security and integrity of the system.

Overall, these three services work together to provide a comprehensive and secure platform for managing sensor data, member profiles, and user authentication within your Spring Boot API.

### Database Management System (DBMS)

You can select the DBMS in spingboot configuration file `application.properties`

#### H2 Database (in developpment)

   - Database configuration for H2

```
# H2 Database configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver
# Hibernate configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=create
# H2 console configuration
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

#### POSTGRES Database (deployment)

   - Database configuration for POSTGRES

```
# Postgres Database
spring.datasource.url= jdbc:postgresql://database_service:5432/sensorsdb
spring.datasource.username= admin
spring.datasource.password= adminadmin
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation= true
spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.PostgreSQLDialect
# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

if we dont use H2 database, we need to insert data in the roles table of our database by excuting these lines : 

```sql
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
```

### Classes Diagram

![Classes Diagram](https://github.com/kindy235/mysignals/blob/main/images/api.png?raw=true "MySignals device and Sensors")

## Services Deployment 

### Docker compose file `docker-compose.yml`


```sh
docker compose down && docker compose build && docker compose up -d
```

To verify if services are launch, use `docker compose ps` command: 

```sh
docker compose ps
NAME                COMMAND                  SERVICE             STATUS              PORTS
api_services        "/bin/sh -c 'java -j…"   api_services        running
database_service    "docker-entrypoint.s…"   database_service    running             0.0.0.0:5432->5432/tcp
pgadmin4            "/entrypoint.sh"         pgadmin             running             443/tcp, 0.0.0.0:5050->80/tcp
proxy_service       "httpd-foreground"       proxy_service       running             0.0.0.0:8000->80/tcp
```

### Github Pipeline (TODO)


## Android Application

### Requirements

- IntelliJ IDEA, Android Studio
- Java JDK & JRE (Version >= 8)
- Android Device(android minimum SDK >= 4.3.0)

### Architecture

The design Pattern use for android developement is MVVM

![API Launcher](https://journaldev.nyc3.digitaloceanspaces.com/2018/04/android-mvvm-pattern.png "MySignals device and Sensors")

   - M : Model
   - V : View
   - VM : ViewModel 

### Classes Diagram

![Classes Diagram](https://github.com/kindy235/mysignals/blob/main/images/mysignalsapp.png?raw=true "MySignals device and Sensors")

## Demo

### API

- Launch the API

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/launch-api.png?raw=true "MySignals device and Sensors")

- Inteface Web screen : http://localhost:8080/swagger-ui/index.html#/

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/api-interface.png?raw=true "MySignals device and Sensors")

**Authentication Service**

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/auth-service.png?raw=true "MySignals device and Sensors")

1. Registration

 - Client Request
  
![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/register.png?raw=true "MySignals device and Sensors")

 - API Response

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/register-response.png?raw=true "MySignals device and Sensors")

1. Login

 - Client Request

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/login-request.png?raw=true "MySignals device and Sensors")

 - API Response

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/login-response.png?raw=true "MySignals device and Sensors")

**How to use the user `AccesToken` to make HTTP Request to the API**

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/member-create2.png?raw=true "MySignals device and Sensors")

**Member Service**

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/member-service.png?raw=true "MySignals device and Sensors")

1. Create Operation : add a Member (POST)

- Request 

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/member-create1.png?raw=true "MySignals device and Sensors")

- Response

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/member-create3.png?raw=true "MySignals device and Sensors")

1. Read Operation : get a Member or All member ()

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/member-get.png?raw=true "MySignals device and Sensors")

3. Delele Operation (HTTP DELETE) : delete a member by id

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/member-delete.png?raw=true "MySignals device and Sensors")

4. Update

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/member-put.png?raw=true "MySignals device and Sensors")

**Sensor Service**

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/sensor-service.png?raw=true "MySignals device and Sensors")

1. Create

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/sensor-create.png?raw=true "MySignals device and Sensors")

2. Read

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/sensor-read.png?raw=true "MySignals device and Sensors")


3. Delele

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/sensor-delete.png?raw=true "MySignals device and Sensors")

4. Update

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/sensor-put.png?raw=true "MySignals device and Sensors")

### MySignals Bluetooth Device

   - Activate Bluetooth

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/device-activate.png?raw=true "MySignals device and Sensors")

   - Start Monitoring

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/device-monitor.png?raw=true "MySignals device and Sensors")

### Android App

**Installation**

1. Android developpement mode

<img src="https://github.com/kindy235/mysignals/blob/main/images/android-dev-mode.jpg?raw=true" class="custom-image">

1. Android device detection in intelliJ

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/installation1.png?raw=true "MySignals device and Sensors")

3. `My Signals APK` Installation

![API Launcher](https://github.com/kindy235/mysignals/blob/main/images/installation2.png?raw=true "MySignals device and Sensors")

**Authentication via android App**

<img src="https://github.com/kindy235/mysignals/blob/main/images/android-register.jpg?raw=true" class="custom-image">
<img src="https://github.com/kindy235/mysignals/blob/main/images/android-login1.jpg?raw=true" class="custom-image">
<img src="https://github.com/kindy235/mysignals/blob/main/images/android-login2.jpg?raw=true" class="custom-image">

**Data Visualization**

1. Add a member
   
<img src="https://github.com/kindy235/mysignals/blob/main/images/android-member1.jpg?raw=true" class="custom-image">


2. Bluetooth Connexion to MySignal Device, Collect and Save sensors data

<img src="https://github.com/kindy235/mysignals/blob/main/images/android-scan1.jpg?raw=true" class="custom-image">
<img src="https://github.com/kindy235/mysignals/blob/main/images/android-scan2.jpg?raw=true" class="custom-image">
<img src="https://github.com/kindy235/mysignals/blob/main/images/android-btconnect.jpg?raw=true" class="custom-image">

3. Realtime Data Visualization

<img src="https://github.com/kindy235/mysignals/blob/main/images/android-data.jpg?raw=true" class="custom-image">

## Improvements

## Sources

- Documentation : https://www.generationrobots.com/media/mysignals_technical_guide_sw.pdf
- JWT Authentication : https://github.com/bezkoder/spring-boot-spring-security-jwt-authentication.git

- Android/IOS libelium libraries : 
  - http://downloads.libelium.com/mysignals/mysignals_android/MySignalsConnectKit.jar.zip
  - http://downloads.libelium.com/mysignals/mysignals_android/MySignalsConnectKitDoc-android.zip
  - http://downloads.libelium.com/mysignals/mysignals_android/MySignalsConnectTest-android.zip
