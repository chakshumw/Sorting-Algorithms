# Sorting Algorithms JavaFX Project

A JavaFX application demonstrating various sorting algorithms with a simple GUI interface.

This guide will help you set up and run the project on **Mac** and **Windows** using IntelliJ IDEA.

---

## **Prerequisites**

1. **Java JDK 21**

   * Download: [Adoptium Temurin JDK 21](https://adoptium.net/temurin/releases/?version=21)
   * Make sure the architecture matches your system: `x64` or `arm64` (Mac Apple Silicon).

2. **JavaFX SDK 21**

   * Download: [Gluon JavaFX SDK 21](https://gluonhq.com/products/javafx/)
   * Extract to a folder you can reference easily (e.g., `/Users/username/javafx-sdk-21` on Mac or `C:\javafx-sdk-21` on Windows).

3. **IntelliJ IDEA** (Community or Ultimate)

---

## **Setup Steps**

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/Sorting-Algorithms.git
cd Sorting-Algorithms
```

---

### 2️⃣ Open in IntelliJ IDEA

1. Open IntelliJ → **File → Open** → Select the cloned folder.
2. Wait for the project to load.

---

### 3️⃣ Configure JavaFX Library

#### Mac:

1. Go to **File → Project Structure → Libraries → + → Java**
2. Select the `lib` folder inside your downloaded JavaFX SDK (e.g., `/Users/username/javafx-sdk-21/lib`)
3. Click **Apply → OK**

#### Windows:

1. Go to **File → Project Structure → Libraries → + → Java**
2. Select the `lib` folder inside your JavaFX SDK (e.g., `C:\javafx-sdk-21\lib`)
3. Click **Apply → OK**

---

### 4️⃣ Set VM Options for Run Configuration

1. Go to **Run → Edit Configurations → Your Main class**
2. In **VM Options**, add:

**Mac Example:**

```text
--module-path /Users/username/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml
```

**Windows Example:**

```text
--module-path C:\javafx-sdk-21\lib --add-modules javafx.controls,javafx.fxml
```

3. Click **Apply → OK**

---

### 5️⃣ Build and Run

1. Rebuild project: **Build → Rebuild Project**
2. Run `Main.java`
3. You should see a window saying **"JavaFX is Working!"**

---

## **Optional Tips**

* For easier setup, you can include the JavaFX `lib` folder in the repo itself.
* For future projects, consider using **Maven** or **Gradle** for automatic dependency management.

---

### **Contact / Help**

If you face issues, open an **issue on this repository** or contact the maintainer.

---

**Happy coding!**
