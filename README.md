# Contact Manager

An offline Android app to save, search and reach your contacts.
Course project for **FNI-421 Mobile Computing**, Ain Shams University (2025–2026).

## Features

- **Contact list** sorted by name, with an initials avatar, the phone number and a favorite star on every row, plus an empty state for first use.
- **Live search** by name or phone while you type.
- **Favorites only** switch, remembered between sessions (SharedPreferences).
- **Contact details** with one-tap **Call**, **SMS** and **Email**, and a favorite switch.
- **Add / Edit contact** form (one screen, two modes) with validation:
  - name: at least 2 letters
  - phone: Egyptian mobile number `01XXXXXXXXX`
  - email: optional, but must be valid if entered
  - errors appear under each field, and the first wrong field gets focus
  - typed data is kept when the phone is rotated
  - a two-column landscape layout
- **Delete** with a confirmation dialog.
- **About** screen built with **Jetpack Compose**, showing the app, course and team info.


## Screens

| Activity | What it does |
|---|---|
| `MainActivity` | Contact list (`ListView` + custom `ContactAdapter`), search, favorites filter, opens Add / Details / About |
| `ContactDetailsActivity` | Shows one contact; Call, SMS, Email, favorite, Edit, Delete |
| `AddEditContactActivity` | Add a new contact, or edit one when opened with a `contactId` extra |
| `AboutActivity` | About screen, written only in Jetpack Compose (no XML) |

Navigation: **List → Details → Edit**. The list and details screens reload in `onRestart()`, so changes show up as soon as you go back.

## Database

One Room table, `contacts`:

| Column | Type |
|---|---|
| `id` | Int, primary key, auto-generated |
| `name` | String |
| `phone` | String |
| `email` | String (can be empty) |
| `isFavorite` | Boolean |

`ContactDao` has `insertContact`, `updateContact`, `deleteContact`, `getAllContacts`, `getContactById` and `searchContacts(query, favOnly)`. All of them are `suspend` functions and are called off the main thread.


## How to run

1. Install **Android Studio** (a recent version that supports AGP 9).
2. Clone the repo:
   ```bash
   git clone https://github.com/ziadshady15/Mobile-Project-26
   ```
3. In Android Studio, choose **File → Open** and pick the `Mobile-Project-26` folder. Wait for Gradle sync to finish (the first sync needs internet).
4. Start an emulator (or plug in a phone with USB debugging on), then press **Run ▶**.

## Team

| Name | ID | Part |
|---|---|---|
| Ziad Shady Nabil | 2023170235 | Project setup, Contact Details screen |
| Fayrouz Waleed Farid | 2023170433 | Contact Details screen (database connection) |
| Abdalah Mohamed Abdo | 2023170360 | Add / Edit Contact screen, validation, landscape layout |
| Marina Joseph Ammar | 2023170465 | Contact list, search, favorites filter, adapter |
| Ronaldo Rafik Rene | 2023170228 | Room database (entity, DAO, database) |
| Mina Maged Adel | 2023170654 | About screen (Jetpack Compose) |

We worked with feature branches and GitHub pull requests: each part was built on its own branch and merged into `main`.
