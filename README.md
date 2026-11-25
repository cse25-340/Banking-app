# Banking System (JavaFX + H2)

## Run in GitHub Codespaces
1. Open the Codespace and wait until it finishes setting up.
2. In the terminal (Ctrl+`):  `bash run.sh`
3. Open the **Ports** tab, click the globe next to port **6080**, password `vscode`.
   The Banking System window appears in that browser tab.

Admin login: `admin` / `admin123`

## Console programs (no window needed)
- Database test + 10 sample customers: `mvn -q exec:java -Dexec.mainClass=bank.dao.TestDB`
- Model check:      `mvn -q exec:java -Dexec.mainClass=bank.CoreModelDemo`
- Controller check: `mvn -q exec:java -Dexec.mainClass=bank.ControllerDemo`

## Run in IntelliJ
Open the folder as a Maven project, then run `bank.Launcher`.

The H2 database file is created in `./data/`.
