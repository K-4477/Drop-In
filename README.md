# 📍 Drop In

> **An Android location management application built with Java and SQLite, allowing users to record, rate and organise places they've visited.**

---

## 📱 About

**Drop In** is an Android application designed to make it easy for users to record and revisit information about places they've been.

Users can create location entries containing a **name, address, country, GPS coordinates, date visited and personal rating**. The information is stored locally using an SQLite database and can be viewed and sorted within the application.

The project combines Android UI development, user input handling, location services, accessibility considerations and local database management into a single application.

---

## ✨ Features

### 📍 Location Recording

* Create new location entries
* Enter a location name, address and country
* Manually enter GPS coordinates
* Automatically populate GPS coordinates using the device's current location
* Save locations to a local SQLite database

### 📅 Date & Rating

* Interactive calendar-based date picker
* Record the date a location was visited
* Rate locations from **1–10** using a SeekBar

### 🗃️ Saved Locations

* View previously saved locations
* Display location information including:

  * Name
  * Address
  * Country
  * Date
  * Rating
  * GPS coordinates
* Sort saved locations from **highest to lowest rating** or **lowest to highest rating**

### ♿ Accessibility

Drop In includes an accessibility control designed to improve usability for users with colour vision deficiencies.

The saved locations screen supports:

* Default colour mode
* Protanopia
* Deuteranopia
* Tritanopia

The accessibility control allows the user to cycle between these modes and change the text colour accordingly.

The application was specifically designed with accessibility and ease of navigation in mind.

---

## 🖼️ Screenshots

### Home Screen

The main screen provides straightforward navigation to either view saved locations or create a new location.

![Drop In Home Screen](/DropIn.png)

---

### Add New Location

The location form allows users to enter information about a place, automatically retrieve GPS coordinates, select a visit date and assign a rating.

![Drop In Location Form](/LocationForm.png)

---

### Date Picker

An interactive date picker allows users to select the date they visited a location using a calendar interface rather than manually entering a date.

![Drop In Date Picker](/Date.png)

---

### Saved Locations

Saved entries are displayed with their stored information, while the sorting control allows locations to be ordered by rating.

The accessibility control is also available from the toolbar.

![Drop In Saved Locations](/SavedLocations.png)

---

### Accessibility

The Saved Locations screen includes an accessibility control that allows users to change the colour scheme of the displayed information.

The application supports multiple colour-vision modes:

* Default
* Protanopia
* Deuteranopia
* Tritanopia

The button allows users to cycle through the available modes and select the option that provides the clearest display for them.

![Drop In Accessibility Display](/Accessibility.png)

---

### Updating an Existing Location

Drop In also supports updating information for an existing saved location.

Users can select an existing location and modify its stored information before saving the changes back to the database.

![Drop In Updating an Existing Location](/UpdateExisting.png)

---

## 🛠️ Tech Stack

| Technology           | Use                              |
| -------------------- | -------------------------------- |
| **Java**             | Application logic                |
| **Android Studio**   | Development environment          |
| **Android SDK**      | Android application framework    |
| **XML**              | UI layouts                       |
| **SQLite**           | Local database                   |
| **SQLiteOpenHelper** | Database creation and management |
| **SQLiteDatabase**   | Database operations              |
| **ContentValues**    | Preparing data for insertion     |

---

## 🏗️ Application Architecture

The core flow of the application is:

```text
                    ┌─────────────────┐
                    │   Android UI    │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │  User Input     │
                    │  & Validation   │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │  Java Logic     │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ ContentValues   │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ SQLiteDatabase  │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ LOCATION Table  │
                    └─────────────────┘
```

When a user saves a location, the application collects the values from the form and places them into a `ContentValues` object. The data is then inserted into the SQLite database.

---

## 🗄️ Database

Drop In uses a local SQLite database containing a `LOCATION` table.

| Column             | Type    | Description                         |
| ------------------ | ------- | ----------------------------------- |
| `_id`              | INTEGER | Automatically generated primary key |
| `LOCATION_NAME`    | TEXT    | Name of the location                |
| `LOCATION_ADDRESS` | TEXT    | Address of the location             |
| `COUNTRY`          | TEXT    | Country                             |
| `DATE`             | TEXT    | Date visited                        |
| `GPS_X`            | FLOAT   | GPS X coordinate                    |
| `GPS_Y`            | FLOAT   | GPS Y coordinate                    |
| `RATING`           | INTEGER | Location rating                     |

The database allows location records to persist between application sessions.

---

## 💻 Example Database Insert

A location is prepared for insertion using Android's `ContentValues` class:

```java
ContentValues values = new ContentValues();

values.put("LOCATION_NAME", locationName);
values.put("LOCATION_ADDRESS", locationAddress);
values.put("COUNTRY", country);
values.put("DATE", date);
values.put("GPS_X", gpsX);
values.put("GPS_Y", gpsY);
values.put("RATING", rating);

long id = db.insert("LOCATION", null, values);
```

This creates the connection between the application's UI and its local database.

---

## 🧠 What I Learned

Building Drop In gave me practical experience across several areas of Android development.

### Android Development

* Creating Android activities
* Designing XML layouts
* Connecting UI components to Java
* Handling button interactions with `setOnClickListener`
* Retrieving and validating user input
* Navigating between application screens
* Working with Android widgets

### Database Development

* Designing a SQLite database
* Creating database tables
* Using primary keys
* Inserting records
* Retrieving stored records
* Sorting database results
* Working with `SQLiteOpenHelper`
* Working with `SQLiteDatabase`
* Using `ContentValues`

### Device Features

* Retrieving GPS coordinates
* Using device location information
* Implementing an interactive date picker

### Accessibility

One of the more interesting aspects of the project was considering accessibility during development.

The saved locations screen includes alternative colour modes for different types of colour vision deficiency, allowing the user to cycle between different display options.

---

## 🔍 Problem Solving & Debugging

Throughout development, I encountered and resolved issues involving:

* SQLite database creation
* SQL syntax
* Java data types
* Database insertion
* User input conversion
* Date formatting
* Android UI components
* Database queries
* Connecting UI events to database operations

Working through these issues helped me understand how the different layers of an Android application interact rather than simply relying on pre-built functionality.

---

## 🚧 Current Status

**Drop In is a completed core application with opportunities for further development.**

The implemented application provides location creation, GPS coordinate handling, date selection, ratings, database storage, saved-location viewing, sorting and accessibility functionality.

One feature that was not completed was editing and saving an existing database entry. This remains a potential area for future development.

---

## 🔮 Future Development

Potential improvements include:

* [ ] Edit existing locations
* [ ] Delete locations
* [ ] Search saved locations
* [ ] Filter locations
* [ ] Display locations on an interactive map
* [ ] Automatically retrieve and validate GPS coordinates
* [ ] Add a graphical star-rating system
* [ ] Add images to location entries
* [ ] Add video support
* [ ] Improve accessibility with adjustable text size
* [ ] Add high-contrast mode
* [ ] Add text-to-speech support
* [ ] Improve UI/UX
* [ ] Add automated testing
* [ ] Explore Room Database
* [ ] Explore MVVM architecture

A particularly interesting future feature would be attaching **photos or videos to saved locations**, turning Drop In into more of a personal travel and memories application. This was also identified as a potential extension during development.

---

## 🚀 Getting Started

### Requirements

* Android Studio
* Android SDK
* Java
* Android Emulator or physical Android device

### Installation

Clone the repository:

```bash
git clone https://github.com/YOUR-USERNAME/drop-in.git
```

Open the project in Android Studio and allow Gradle to synchronise.

Connect an Android device or start an Android Emulator.

Select:

**Run ▶**

Android Studio will build and install Drop In onto the selected device.

---

## 📂 Repository Structure

```text
Drop-In/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           ├── res/
│           │   ├── layout/
│           │   ├── drawable/
│           │   └── values/
│           │
│           └── AndroidManifest.xml
│
├── screenshots/
│   ├── home-screen.png
│   ├── location-form.png
│   ├── date-picker.png
│   └── saved-locations.png
│
├── README.md
└── ...
```

---

## 🎯 Project Goals

The main goal of Drop In was to build a functional Android application that brings together:

**User Interface → Application Logic → Device Features → Database → Accessibility**

Rather than focusing on one isolated Android feature, the project provided experience working across multiple parts of an application and connecting them together into a functional product.

---

## 👨‍💻 Developer

**[Your Name]**

I'm a software development enthusiast building practical projects to develop my skills in application development, databases, problem solving and software engineering.

I'm particularly interested in continuing to develop my skills in **Android development, Java, databases and modern software development practices**.

### Connect with me

* GitHub: `https://github.com/YOUR-USERNAME`
* LinkedIn: `https://linkedin.com/in/YOUR-PROFILE`

---

## 📄 License

This project is primarily intended as a portfolio and educational project.
