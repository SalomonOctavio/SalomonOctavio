FAC_Portafolio M7/                 ← 📂 Carpeta raíz del repositorio
│
├── .github
├── docs
├── pom.xml                        ← 📝 POM padre (packaging = pom)
│
├── usuarios/                      ← 📂 Módulo Usuarios
│   ├── pom.xml                    ← 📝 POM hijo (hereda del padre)
│   └── src/
│       ├── main/java/             ← 📂 Código fuente
│       └── test/java/             ← 📂 Tests
│           └── SanityTest.java
│
├── pagos/                         ← 📂 Módulo Pagos
│   ├── pom.xml
│   └── src/
│       ├── main/java/
│       └── test/java/
│           └── SanityTest.java
│
├── inventario/                    ← 📂 Módulo Inventario
│   ├── pom.xml
│   └── src/
│       ├── main/java/
│       └── test/java/
│           └── SanityTest.java
│
└── notificaciones/                ← 📂 Módulo Notificaciones
    ├── pom.xml
    └── src/
        ├── main/java/
        └── test/java/
            └── SanityTest.java
