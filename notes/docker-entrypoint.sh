#!/bin/bash

set -e

echo "======================================"
echo "🚀 Note Application Starting"
echo "======================================"

# Fonction pour vérifier si PostgreSQL est prêt
wait_for_postgres() {
    echo "⏳ Waiting for PostgreSQL to be ready..."
    max_attempts=30
    attempt=0

    while [ $attempt -lt $max_attempts ]; do
        if timeout 1 bash -c "cat < /dev/null > /dev/tcp/postgres/5432" 2>/dev/null; then
            echo "✅ PostgreSQL is ready!"
            return 0
        fi
        echo "   PostgreSQL is unavailable - sleeping (attempt $((attempt+1))/$max_attempts)"
        sleep 2
        attempt=$((attempt+1))
    done

    echo "❌ PostgreSQL did not become ready in time"
    exit 1
}

# Attendre PostgreSQL
wait_for_postgres
# Attendre encore un peu
sleep 10

echo "======================================"
echo "🌐 Starting FrontRest API on port 8082 (DEBUG MODE)"
echo "======================================"
java -jar frontrest.jar \
    --server.port=8082 \
    --spring.datasource.url=${SPRING_DATASOURCE_URL} \
    --spring.datasource.username=${SPRING_DATASOURCE_USERNAME} \
    --spring.datasource.password=${SPRING_DATASOURCE_PASSWORD} \
    --spring.kafka.bootstrap-servers=${SPRING_KAFKA_BOOTSTRAP_SERVERS} \
    --debug &

FRONTREST_PID=$!
echo "✅ FrontRest started with PID: $FRONTREST_PID"


echo ""
echo "======================================"
echo "✅ All services started!"
echo "======================================"

# Fonction de nettoyage
cleanup() {
    echo ""
    echo "======================================"
    echo "🛑 Shutting down applications..."
    echo "======================================"
    kill $ $FRONTREST_PID 2>/dev/null
    wait $ $FRONTREST_PID 2>/dev/null
    echo "✅ Shutdown complete"
    exit 0
}

trap cleanup SIGTERM SIGINT

# Attendre que les processus se terminent
wait $FRONTREST_PID