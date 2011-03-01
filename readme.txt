
# lokal starten
mvn clean package exec:java

# release bauen
mvn clean package dependency:copy-dependencies assembly:assembly

# test

