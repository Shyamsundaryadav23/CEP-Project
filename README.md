# 🏥 Doctor Appointment App

An Android application built with **Kotlin** and **Firebase** that enables patients to book doctor appointments, view available time slots, and get medical help. Doctors can manage their availability, appointments, and patient records.

---

## 📱 Features

### 👤 Patient Side
- 🔍 View list of available doctors
- 🗓 Book appointments with time slot selection
- ⏰ Receive reminders for medicines
- 🚨 One-click emergency assistance
- 📋 View personal profile and edit details

### 👨‍⚕️ Doctor Side
- 🗂 Manage appointments and availability
- 📅 Add/edit/delete available time slots for multiple dates
- 🧑‍⚕️ View patient history and consultation records
- 🚦 Toggle availability status

### 🔔 Notifications
- 📲 Push notifications when appointments are booked (FCM)
- 🔁 Real-time updates for doctors and patients

---

## 🔧 Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Kotlin |
| UI | Material Design |
| Backend | Firebase Authentication, Firestore, Realtime Database |
| Push Notifications | Firebase Cloud Messaging (FCM) |
| Offline Support | Room Database |
| Navigation | BottomNavigationView |

---

## 🗃 Firestore Structure (Simplified)
/doctors/{doctorId}
- name, email, phone, etc.
/availability/{date}
- date: "yyyy-MM-dd"
- slots: ["09:00", "10:00", "11:30"]

/patients/{patientId}
- name, phone, email, etc.

/appointments/{appointmentId}
- doctorId, patientId, date, time, status

---

## 🚀 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/your-username/doctor-appointment-app.git
2. Open in Android Studio
Open the project folder.

Sync Gradle files.

3. Firebase Setup
Create a project in Firebase Console

Add Android app to Firebase with your package name

Download google-services.json and place it in app/

Enable the following:

Authentication (Email/Password, Phone)

Firestore Database

Realtime Database

Firebase Cloud Messaging (for notifications)

4. Add Firebase SDK
Already added in build.gradle:

groovy
Copy code
implementation 'com.google.firebase:firebase-auth'
implementation 'com.google.firebase:firebase-database'
implementation 'com.google.firebase:firebase-firestore'
implementation 'com.google.firebase:firebase-messaging'
🧪 Testing
Test as a patient: register and book appointments.

Test as a doctor: view availability, manage slots and patients.

Use Firebase Console to monitor data changes and FCM delivery.

📦 Features Coming Soon
🧾 Prescription generator (PDF)

📍 Doctor location map

💬 In-app chat (doctor ↔ patient)

🧠 AI queue prediction system

🙌 Contributing
Feel free to fork and submit PRs! Open issues for bugs and feature requests.

📷 Screenshots

![IMG-20250401-WA0010 1](https://github.com/user-attachments/assets/92adef1d-1109-4158-b85b-590a63fa202c)
![IMG-20250401-WA0011 1](https://github.com/user-attachments/assets/c8156959-ab0f-41c4-bb92-6ac769c8dead)
![IMG-20250401-WA0012 1](https://github.com/user-attachments/assets/8dccb3ad-3cb9-4fa4-b395-3e557b9d56c0)
![IMG-20250401-WA0013 1](https://github.com/user-attachments/assets/a4ac2c98-a89d-48c1-92fa-5ee37fed6afb)
![IMG-20250401-WA0015 1](https://github.com/user-attachments/assets/9ccc04c2-07f4-4511-9e33-5292196c4ead)
![IMG-20250401-WA0016 1](https://github.com/user-attachments/assets/fe3ac4f5-96e2-4e38-8b6b-eaa3a28a5b56)



