# Train Ticket Booking System

A Java-based console application that simulates the train ticket booking system. This application allows users to manage train bookings, search for trains, and handle user authentication.

## 🚂 Features

- **User Management**
  - User registration and login
  - Secure password handling using BCrypt
  - Session management
  - User profile management

- **Train Search**
  - Search trains by source and destination
  - View detailed train schedules
  - Real-time seat availability
  - Station listing

- **Booking Management**
  - Interactive seat selection
  - Ticket booking with date selection
  - View booking history
  - Ticket cancellation
  - Seat layout visualization

## 🛠️ Technical Stack

- **Language**: Java 21
- **Build Tool**: Gradle 8.9
- **Dependencies**:
  ```gradle
  implementation 'com.fasterxml.jackson.core:jackson-databind:2.15.3'
  implementation 'org.mindrot:jbcrypt:0.4'
  implementation 'com.google.guava:guava:33.1.0-jre'
  implementation 'com.google.code.gson:gson:2.10.1'
  testImplementation 'junit:junit:4.13.2'
  ```

## 📦 Project Structure

```
IRCTC/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── ticket/
│   │   │   │       └── booking/
│   │   │   │           ├── entities/
│   │   │   │           ├── services/
│   │   │   │           ├── util/
│   │   │   │           └── App.java
│   │   │   └── resources/
│   │   │       └── localDb/
│   │   │           ├── trains.json
│   │   │           └── users.json
│   │   └── test/
│   └── build.gradle
└── gradle/
```

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 21 or higher
- Gradle 8.9 or higher

### Setup & Installation

1. Clone the repository:
```bash
git clone https://github.com/piyush-ghanghav/Ticket-Booking.git
cd Ticket-Booking
```

2. Build the project:
```bash
./gradlew build
```

3. Run the application:
```bash
./gradlew run
```

## 📱 Usage Guide

### Main Menu Options
1. **Sign up** - Create new account
2. **Login** - Access existing account
3. **Fetch Bookings** - View booking history
4. **Search Trains** - Find available trains
5. **Book a Seat** - Make new reservation
6. **Cancel Bookings** - Cancel existing reservation
7. **Logout** - Exit current session
8. **Exit** - Close application

### Booking Process
1. Login to your account
2. Search for trains using source and destination
3. Select preferred train from available options
4. Choose seat from visual layout
5. Enter travel date (format: dd-mm-yyyy)
6. Confirm booking

## 💾 Data Storage

The application uses JSON files for data persistence:

### Users Data Structure ([`users.json`](app/src/main/resources/localDb/users.json))
```json
{
  "name": "username",
  "hashedPassword": "[bcrypt_hash]",
  "ticketsBooked": [],
  "userId": "[uuid]"
}
```

### Trains Data Structure ([`trains.json`](app/src/main/resources/localDb/trains.json))
```json
{
  "trainId": "T123",
  "trainNo": "12345",
  "seats": [[0,1,0], [1,0,1]],
  "stationTime": {
    "mumbai": "06:30:00",
    "pune": "09:15:00"
  },
  "stations": ["mumbai", "pune"]
}
```

## 🔒 Security Features

- Password hashing using BCrypt
- UUID-based ticket and user identification
- Session management for booking operations
- Input validation and sanitization

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

