# I AM PRESENT

# Attendance Tracking App

## Introduction

I AM PRESENT is a mobile application designed to streamline the attendance recording process. The app is aimed at providing a more efficient, reliable, and user-friendly solution for coordinators, lecturers, and students.

## Background & Motivation

The current manual method of recording attendance using paper sheets at LNBTI poses various challenges, including time and resource consumption, storage hassles, and reporting difficulties. I AM PRESENT App addresses these issues by introducing a digital solution, allowing for easier management and accessibility of attendance records.

## Flavors

I AM PRESENT App comes in two flavors:

1. **Coordinator Flavor**
- Designed for coordinators for managing lecture details using their android smartphones.
- Features include creating/deleting lecture sessions and opening/closing respective lecture sessions to mark attendance by students.
- Additional administrative feature for exporting attendance reports.

2. **Student Flavor**
  - Designed for students to conveniently mark their attendance using their android smartphones.
  - Features include scanning/generating QR codes to mark attendance for respective lectures.

## Getting Started

### Coordinator Flavor

  <img src="docs/coordinator-demo.gif" width="320">

#### Actions

- **To save new lecture**
  1. Click `+` floating button.
  2. Enter details of conducting lecture's batch, semester, subject, location, start date and time, end date and time, lecturer's name and email.
  3. Click save button.

- **To delete saved lecture**
  1. Select respective lecture.
  3. Click Delete lecture button.

- **To open lecture for attendance**
  1. Select respective lecture.
  3. Click Open for attendance button. 

- **To stop marking attendance for lecture**
  1. Select respective lecture.
  3. Click Close for attendance button.

- **To see today's lectures only**
  1. Go to Today menu.

- **To export reports of attendance**
  1. Go to Reports menu.
  2. Select required filter option, i.e batch, date, student, lecturer, lecture status or location.
  3. Select required result option from dropdown.
  4. Click Export result button.

### Student Flavor

  <img src="docs/student-demo.gif" width="320">

#### Actions

- **To mark attendance at lecture**
  1. Click QR floating button.
  2. Scan QR code of the respective lecture from either coordinator's app or student's app.
  3. Click Yes button from popup to confirm attendance.

### Technologies Adopted

- **Mobile App Development**
     - IDE：Android Studio Giraffe | 2022.3.1
     - Kotlin：1.9.10
     - Java：17
     - Gradle：7.3.1
     - minSdk：29
     - targetSdk：34
     - firebase crashlytics
- **Backend Development**
    - Java
    - Spring Boot
- **Database**
    - MySQL
- **Hosting and Cloud Services**
    - AWS

## Future Enhancements

Identify potential future features and improvements such as,

- The app to operate offline, allowing attendance to be recorded without an internet connection.
- The lecture schedule to be synchronized with all coordinators and lectures so that the app could be used to view the allocated classrooms and lecturers to prevent clashes when scheduling lectures.


