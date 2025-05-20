# ProjectTask

Proyecto Kotlin Multiplatform (KMP) orientado a la gestión de tareas, con soporte para escritorio y web.

## Estructura del proyecto

- **/composeApp**: Código principal multiplataforma (Kotlin/Compose).
  - **src/commonMain**: Código y lógica compartida entre plataformas.
  - **src/desktopMain**: Código específico para escritorio.
- **/ProjectTaskWeb**: Código fuente para la versión web (si aplica).
- **/scripts**: Scripts utilitarios (por ejemplo, `decrypt-tasks.ps1` para desencriptar tareas).
- **/tools**: Herramientas externas (por ejemplo, `wix311` para instaladores Windows).
- **/docs**: Documentación y esquemas.
- **/config**: Archivos de configuración global (por ejemplo, `gradle.properties`).

## Scripts útiles

- `scripts/decrypt-tasks.ps1`: Desencripta el archivo de backup de tareas. Uso:
  ```
  pwsh scripts/decrypt-tasks.ps1 -Password "tu_contraseña"
  ```

## Personalización de colores

Los colores personalizados y el esquema de color están centralizados en:
- `composeApp/src/desktopMain/kotlin/ui/AppColors.kt`

## Requisitos
- JDK 17+
- Gradle (wrapper incluido)
- [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)

## Compilar y ejecutar

Para compilar la app de escritorio:
```sh
./gradlew :composeApp:run
```

---

Para más información sobre Kotlin Multiplatform, visita la [documentación oficial](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html).