# Caminho para o JAR gerado pelo builder
JAR=target/gs-gateway-0.0.1-SNAPSHOT.jar

# Comando principal: builda (se necessário) e sobe o gateway
build-and-run: $(JAR)
	docker compose up gateway

# Regra para buildar o JAR
$(JAR):
	docker compose run --rm builder
