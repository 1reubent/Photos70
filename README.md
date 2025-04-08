# Project Checklist

---

## ✅ **Project Setup**
- [x] Create a Java project called `PhotosXX` (XX = group number).
- [x] Set up standard Java project structure:
  - [x] `src/`
  - [x] `data/` (for stock photos)
  - [x] `docs/` (for Javadoc)
- [x] Create `Photos.java` as the main entry point.
- [x] Use **JavaFX** and **FXML** for GUI.
- [x] Do **not** use external libraries.
- [x] Initialize a **private GitHub repo**, add grader access.
- [x] First commit from **each partner** before March 28.
- [x] Make incremental commits throughout development.

---

## ✅ **Model Design (Data Layer)**
- [x] Use **java.io.Serializable** for model classes.
- [x] Use **ObjectOutputStream/ObjectInputStream** for persistence.
- [ ] Model Classes:
  - [x] `User`: username, list of albums.
  - [x] `Album`: name, list of photos.
  - [x] `Photo`: file path, caption, date, list of tags.
  - [ ] `Tag`: name, value.
  - [ ] `Admin`: handles user creation/deletion.
  - [ ] `TagType`: pre-defined and user-defined tag types.
- [ ] Ensure:
  - [ ] Proper equals/hashCode for comparing tags and photos.
  - [ ] Serializable versioning (serialVersionUID).
- [x] For dates, use either:
  - [ ] `Calendar` (with `.set(Calendar.MILLISECOND, 0)`)
  - OR
  - [x] `java.time.LocalDateTime` / `java.time.Instant`
- [x] Separate **model package** from controller and view.

---

## ✅ **Persistence**
- [x] Serialize **users**, **albums**, and **photos** on disk.
- [x] Ensure **saving** data at:
  - [x] User logout
  - [x] Application quit
- [x] On startup, **load** existing data from disk.
- [ ] Save user photos only as **file paths**, do **not** copy user images to workspace.
- [x] Store stock photos in `/data/` directory.

---

## ✅ **User System**
- [x] Implement login screen:
  - [x] Username field
  - [ ] (Optional) Password field
- [ ] Recognize:
  - [ ] `admin` user triggers admin subsystem
  - [x] `stock` user loads stock photos
- [x] Non-admin users load their albums and photos on login.

---

## ✅ **Admin Subsystem**
- [ ] List existing users.
- [ ] Create new user.
- [ ] Delete existing user.
- [ ] Use GUI only (no console).
- [ ] Validate:
  - [ ] No duplicate usernames.
  - [ ] Handle errors gracefully in GUI.

---

## ✅ **Non-Admin User Subsystem**

### Album Management
- [x] Display albums (name, photo count, date range).
- [x] Create new album (unique per user).
- [ ] Rename album. (still need to check that it works)
- [ ] Delete album. (still need to check that it works)
- [ ] Open album to see photo thumbnails and captions.

### Photo Management (inside album view)
- [x] Add photo (select from file system).
- [ ] Ensure:
  - [x] For stock photos: from `/data/`
  - [ ] For user photos: path is stored, photo not copied
- [ ] Remove photo.
- [ ] Caption / recaption photo.
- [ ] View photo:
  - [x] Show full image.
  - [ ] Show caption.
  - [ ] Show date (file's last modified date).
  - [ ] Show all tags.

### Tag Management (inside album view)
- [ ] Predefined tag types (e.g., location, person).
- [ ] Allow user to define new tag types.
- [ ] Add tag to photo:
  - [ ] Enforce rules:
    - [ ] Single value tags (e.g., location).
    - [ ] Multi-value tags (e.g., person).
    - [ ] No duplicate tag name+value pairs.
- [ ] Delete tag from photo.

### Photo Copy/Move (inside album view)
- [ ] Copy photo to another album (same physical photo).
- [ ] Move photo to another album (remove from source).

### Slideshow (inside album view)
- [ ] Navigate photos in album, forward and backward.

### Search (inside album view)
- [ ] By **date range**.
- [ ] By **tag**:
  - [ ] Single tag-value pair.
  - [ ] Conjunctive (AND) of two tag-value pairs.
  - [ ] Disjunctive (OR) of two tag-value pairs.
- [ ] Create album from search results.

---

## ✅ **Stock Photos**
- [x] Select 5–10 medium-res images.
- [x] Store in `/data/` directory.
- [x] Create **stock user** (username: `stock`, password optional).
- [x] Store stock photos in stock album under stock user.
- [x] Leave stock photos in workspace (for grader access).

---

## ✅ **GUI (View Layer)**
- [x] Use **JavaFX** and **FXML** only.
- [ ] No console input/output.
- [ ] Graceful error handling (alerts, dialogs).
- [ ] User-friendly navigation:
  - [x] Login screen
  - [ ] Admin panel
  - [x] Album list view
  - [ ] Album photo view
  - [ ] Photo detail view
  - [ ] Search results view
- [ ] Manual slideshow controls.

---

## ✅ **General App Behavior**
- [x] **Logout:**
  - [x] Save session data.
  - [x] Return to login screen.
- [x] **Quit application:**
  - [x] Ensure data is saved.
  - [x] Application closes cleanly.
- [ ] Prevent duplicate album names **within user**.
- [ ] Allow duplicate album names **across users**.
- [ ] Allow multi-user operation:
  - [ ] After logout, allow new user login without app restart.

---

## ✅ **Documentation**
- [ ] Javadoc for **every class**.
- [ ] Include:
  - [ ] Author tag
  - [ ] Description of class purpose
  - [ ] Method-level comments
- [ ] Generate Javadoc and place in `/docs/`.

---

## ✅ **Testing**
- [ ] Test admin functions (create, delete users).
- [ ] Test login/logout flows.
- [ ] Test album creation, rename, delete.
- [ ] Test adding/removing photos.
- [ ] Test captions.
- [ ] Test tags (add/delete, single/multi-value).
- [ ] Test copy/move photos.
- [ ] Test manual slideshow.
- [ ] Test search (all types).
- [ ] Test album from search results.
- [ ] Test serialization:
  - [ ] Save and reload data.
- [ ] Test error handling:
  - [ ] No crashes.
  - [ ] No unhandled exceptions.
  - [ ] User-friendly error messages.

---

## ✅ **GitHub Final Steps**
- [ ] All changes committed.
- [ ] Ensure private repo has grader access.
- [ ] Push generated Javadoc to `/docs/`.
- [ ] Push stock photos to `/data/`.
- [ ] Ensure no user photos or sensitive info in repo.
- [ ] Add README (optional, helpful for grading).

---

### Optional Improvements:
- [ ] Add password functionality for realism.
- [ ] Add visual flair to GUI (stylesheets, icons).
- [ ] Add confirmation dialogs (e.g., before delete).
- [ ] Responsive/resizable UI.

---

[//]: # (OLD CHECKLIST)

[//]: # (## Things I think we finished:)

[//]: # ()
[//]: # (# Things I think we finished:)

[//]: # ()
[//]: # (- ✅ user login/logout)

[//]: # ()
[//]: # (# Things we still need to do)

[//]: # ()
[//]: # (## Misc:)

[//]: # ()
[//]: # (- admin login/logout)

[//]: # ()
[//]: # (## Models:)

[//]: # ()
[//]: # (- Tag class)

[//]: # (- TagType class)

[//]: # ()
[//]: # (## On the User home page:)

[//]: # ()
[//]: # (- create/add/delete/rename album)

[//]: # (  - check if these work)

[//]: # (    - ✅ create)

[//]: # (    - ✅ delete)

[//]: # (    - add)

[//]: # (    - rename)

[//]: # (  - check to make sure the changes are permanent)

[//]: # ()
[//]: # (## On the Album page:)

[//]: # ()
[//]: # (- Opening an album displays all photos, with their thumbnail images and captions, inside that album.)

[//]: # (- Display Photo:)

[//]: # (  - Display a photo in a separate display area. The photo display should also show its caption, its date-time of)

[//]: # (    capture &#40;see Date of photo below&#41;, and all its tags &#40;see Tags below&#41;.)

[//]: # (    - need to add caption, date, and tags to the photo display)

[//]: # (- All other unimplemented functionality)

[//]: # (  - ✅Add Photo to Album)

[//]: # (    - Check to make sure changes are permanent)

[//]: # (  - ✅Remove Photo)

[//]: # (  - Caption Photo &#40;i think it works&#41;)

[//]: # (  - Add Tag)

[//]: # (    - need to initialize with some tags for the user to choose from)

[//]: # (  - Remove Tag)

[//]: # (  - Copy Photo)

[//]: # (  - Move Photo)

[//]: # (  - SlideShow)

[//]: # (  - Search Photos)

[//]: # (    - create an album using photo search results)

[//]: # ()
[//]: # (## Storing data)

[//]: # ()
[//]: # (- ✅ "You are required to use the java.io.Serializable interface, and the)

[//]: # (  java.io.ObjectOutputStream/java.io.ObjectInputStream classes to store and retrieve data.")

[//]: # (  - "See Notes on Serialization and Versioning to know how to implement serialization and deserialization.")

[//]: # (  - now using one `.dat` to save everything &#40;see app.Photo&#41;)

[//]: # ()
[//]: # (- Save Data at:)

[//]: # (  - ✅ user lgout)

[//]: # (  - application quit)

[//]: # ()
[//]: # (## Admin page:)

[//]: # ()
[//]: # (- not started)

[//]: # ()
[//]: # (## quit:)

[//]: # ()
[//]: # (- not implemented)

[//]: # ()
[//]: # (## Documentation)

[//]: # ()
[//]: # (- The complete Javadoc HTML documentation should be generated and placed in the docs directory.)

[//]: # (- )
