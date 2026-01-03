
```markdown
# Documentation Kafka (Mémo)

Ce document regroupe les commandes essentielles pour gérer Kafka en environnement **Local (Windows)** et **Docker**.

---

## 1. Environnement Local (Windows)

Lancement des services directement via les scripts `.bat` sur la machine hôte.

### Démarrage des Services

**1. Lancer Zookeeper**
> C'est le coordinateur. Il doit toujours être lancé **EN PREMIER**.
> Il gère l'état du cluster Kafka et la configuration.
```bash
bin\windows\zookeeper-server-start.bat config/zookeeper.properties
```
**2. Lancer le Broker Kafka**

> C'est le serveur principal qui stocke et distribue les messages.
> Il se connecte à Zookeeper.

```bash
bin\windows\kafka-server-start.bat config/server.properties
```

### Interactions (Topic R4)

**3. Consommateur Console (Lecture avancée)**

> Lit les messages du topic `R4`.
> * `--property print.key=true` : Affiche la clé.
> * `--property key.deserializer` : Traduit la clé en String.
> 
> 

```bash
bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic R4 --property print.key=true --property print.value=true --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
```

**4. Producteur Console**

> Permet d'écrire des messages dans le topic `R4`.

```bash
bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic R4
```

---

## 2. Environnement Docker

Commandes à exécuter via `docker exec` sur le conteneur du broker (nommé `bdcc-kafka-broker`).

### Commandes de base

**Démarrer l'environnement**

```bash
docker-compose up -d
```

**Lister les topics**

```bash
docker exec --interactive --tty bdcc-kafka-broker kafka-topics --bootstrap-server broker:9092 --list
```

**Producteur Standard (Topic T1)**

```bash
docker exec --interactive --tty bdcc-kafka-broker kafka-console-producer --bootstrap-server broker:9092 --topic T1
```

**Consommateur Standard (Topic T1)**

> ⚠️ Par défaut, ne lit que les **nouveaux** messages.

```bash
docker exec --interactive --tty bdcc-kafka-broker kafka-console-consumer --bootstrap-server broker:9092 --topic T1
```

### Consommation Avancée (Types de données)

**Consommateur avec Deserializers (Topic R66)**

> Utile quand la valeur est un `Long` (nombre) et la clé une `String`.

```bash
docker exec --interactive --tty bdcc-kafka-broker kafka-console-consumer --bootstrap-server broker:9092 --topic T4 --property print.key=true --property print.value=true --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
```

### Gestion de l'historique et des Groupes

Ces commandes permettent de contrôler quels messages sont lus (anciens vs nouveaux).

**1. Lire TOUT l'historique**

> L'option `--from-beginning` force la relecture de tous les messages du topic.

```bash
docker exec --interactive --tty bdcc-kafka-broker kafka-console-consumer --bootstrap-server broker:9092 --topic T1 --from-beginning
```

**2. Utiliser un Groupe de Consommateurs (Persistance)**

> L'option `--group` permet à Kafka de sauvegarder votre position. Si vous arrêtez et relancez, vous reprendrez là où vous vous êtes arrêté.

```bash
docker exec --interactive --tty bdcc-kafka-broker kafka-console-consumer --bootstrap-server broker:9092 --topic T1 --group mon-groupe-A
```

**3. Lire à partir d'un offset précis**

> Lit la partition 0 à partir du message n°10.

```bash
docker exec --interactive --tty bdcc-kafka-broker kafka-console-consumer --bootstrap-server broker:9092 --topic T1 --partition 0 --offset 10
```

```

```