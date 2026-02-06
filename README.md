# 🧩 Affinity Finder

![Java](https://img.shields.io/badge/Java-11-orange?logo=java&logoColor=white)
![Gradle](https://img.shields.io/badge/Build-Gradle-02303A?logo=gradle&logoColor=white)
![Status](https://img.shields.io/badge/Status-Completed-success)

**Affinity Finder** is an application that enables users to create forms and collect responses. Using K-means clustering algorithm, it groups users with similar responses into affinity groups to identify common interests and preferences.

## ⚡ Demo Preview
<!-- El GIF hace que el proyecto entre por los ojos -->
<img src="assets/demo.gif" alt="Affinity Finder Demo" width="350">

🎥 [Click here to watch the full walkthrough with more functionalities](assets/fullDemo.mp4)

## 💡 How It Works

1. **Form Creation**: Users create custom forms with various question types.
2. **Data Collection**: Other users submit their responses through an intuitive interface.
3. **Profile Management**: Admin manage user profiles and permissions, also every user can manage their own profile.
4. **K-means Clustering**: The clustering algorithm its periodically executed. Admin can also manually execute the clustering algorithm to analyze responses.
5. **Affinity Groups**: Users are automatically organized into groups with shared interests.

## 🛠️ Tech Stack

- **Language**: Java 11
- **Build Tool**: Gradle
- **NLP**: OpenNLP for text analysis
- **Algorithms**: K-means clustering, PCA transformation
- **Architecture**: Layered architecture (Domain, Persistence, Presentation)

## 👥 Team Members

- **Eduardo Hernández Pareja** - [@eduardo.hernandez.pareja](https://github.com/eduardo.hernandez.pareja)
- **Álex Manzaneda Marquez** - [@alex.manzaneda](https://github.com/alex.manzaneda)
- **Oriol Valencia Comellas** - [@oriol.valencia](https://github.com/oriol.valencia)
- **Sara Vidal González** - [@sara.vidal](https://github.com/sara.vidal)

## 🚀 Quick Start

```bash
# Clone the repository
git clone https://github.com/your-username/Affinity-Finder.git
cd Affinity-Finder/FONTS

# Run the application
./gradlew run
```

For detailed build instructions, see [UserManual](DOCS/UserManual.pdf).

## 📁 Project Structure

- **[FONTS/](FONTS/)** - Source code and build configuration
- **[EXE/](EXE/)** - Executable JAR and launch scripts
- **[DOCS/](DOCS/)** - User manual and Javadoc API documentation
- **[LICENSE](LICENSE)** - Project license

## 📄 License

This project is licensed under the terms specified in the [LICENSE](LICENSE) file.
