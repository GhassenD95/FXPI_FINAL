# Système de Gestion Sportive

## Aperçu
Ce projet est développé dans le cadre d'une application de gestion sportive complète, construite avec JavaFX. Le système offre une solution intégrée pour la gestion des installations sportives, des équipes, des tournois et du suivi des performances des athlètes, avec des fonctionnalités innovantes comme l'intégration de l'IA et un système de calendrier intelligent.

## Mots clés
JavaFX, Gestion Sportive, Base de Données MySQL, Authentification, API Google Calendar, Chatbot IA, Analyse de Performance, Interface Graphique

## Fonctionnalités

- **Gestion des Utilisateurs**
  - Inscription et authentification des utilisateurs
  - Gestion des profils utilisateur
  - Contrôle d'accès basé sur les rôles

- **Gestion des Installations Sportives**
  - Suivi et gestion des installations sportives 
  - Gestion de l'inventaire des équipements
  - Planification et réservation des installations

- **Gestion des Équipes**
  - Création et gestion des effectifs
  - Profils des joueurs et statistiques
  - Suivi des performances d'équipe

- **Gestion des Tournois**
  - Création et planification des tournois
  - Gestion des matchs
  - Tableaux des tournois et suivi des résultats

- **Analyse des Performances**
  - Suivi des performances des athlètes
  - Analyse statistique et visualisation
  - Graphiques et rapports de performance

- **Intégration du Calendrier**
  - Intégration avec Google Calendar
  - Planification et gestion des événements
  - Notifications automatisées

- **Intégration IA**
  - Chatbot Gemini pour l'assistance utilisateur
  - Analyses intelligentes et recommandations

## Stack Technique

### Frontend
- JavaFX 17 (Interface utilisateur)
- FXML (Structure des vues)
- CSS (Stylisation)
- Ikonli (Packs d'icônes)
- BootstrapFX (Composants UI)

### Backend
- Java 17
- Maven (Gestion de projet)
- MySQL (Base de données)

### Intégrations
- Google Calendar API (Gestion du calendrier)
- API Gemini (Chatbot IA)
- JBCrypt (Cryptage des mots de passe)
- Google ZXing (Génération de QR codes)

### Bibliothèques
- ControlsFX (Composants JavaFX avancés)
- Jackson (Traitement JSON)
- MySQL Connector (Connexion base de données)

## Pour Commencer

### Prérequis
- Kit de Développement Java (JDK) 17 ou supérieur
- Serveur MySQL
- Maven
- Compte Google (pour l'intégration Calendar)

### Installation

1. Cloner le dépôt :
   ```powershell
   git clone [url-du-depot]
   ```

2. Configuration de la base de données :
   - Créer une base de données MySQL
   - Modifier la configuration dans `src/main/resources/config.properties`

3. Installation des dépendances :
   ```powershell
   mvn clean install
   ```

4. Lancement de l'application :
   ```powershell
   mvn javafx:run
   ```

## Structure du Projet

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── controllers/    # Contrôleurs JavaFX
│   │   │   ├── models/        # Modèles de données
│   │   │   ├── services/      # Logique métier
│   │   │   ├── tools/         # Classes utilitaires
│   │   │   └── enums/         # Classes énumérées
│   │   └── resources/
│   │       ├── css/           # Feuilles de style
│   │       ├── images/        # Ressources graphiques
│   │       └── *.fxml         # Fichiers de layout
└── test/
    └── java/                  # Tests unitaires
```

## Remerciements
Ce projet a été développé à Esprit School of Engineering dans le cadre du module PIDEV. Nous remercions particulièrement nos instructeurs et mentors pour leur accompagnement tout au long du projet.
