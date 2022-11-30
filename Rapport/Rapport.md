# Rapport TP Techno Web 2022/2023

`Binôme: GUEYE Serigne Saliou & DIALLO Ismael`

## Table of contents

### I. [Démarrage de l'application](#commands)

### II. [Architecture générale](#architecture)

### III. [UML et Modèle de données](#uml)

- A. [UML](#uml_graph)
- B. [Modèle de données](#mdd)

### IV. [Gestion des ressources](#ressources)

- A. [Schémas URL](#surl)
- B. [Négociation de contenu](#ndc)
- C. [Requêtes conditionnelles](#rc)

<div style="page-break-after: always"></div>

## I. Démarrage de l'application <a name ="commands"></a>

Cette application utilise le framework [Spring Boot](https://spring.io/projects/spring-boot) avec [maven](https://maven.apache.org/) comme environnemnt d'exécution/compilation.Vous pouvez lancer l'application automatiquement sans ligne de commande si vous utilisez un éditeur de texte comme `IntelliJ`.Sinon, vous pouvez utiliser les lignes de commandes.

- Pour lancer l'application :
  ```shell
  $ ./mvnw spring-boot:run
  ```
- Pour lancer les tests
  ```shell
  $ ./mvnw test
  ```

## II. Architecture générale <a name ="architecture"></a>

![architecture.svg](architecture.svg)

<div style="page-break-after: always"></div>

## III. UML et Modèle de données <a name ="uml"></a>

#### A. UML <a name ="uml_graph"></a>

Nous nous sommes basé sur les spécifications fonctionnelles pour créer cet UML:
![img_1.png](img_1.png)

#### B. Modèle de données <a name ="mdd"></a>

##### Compraison Mapping unidirectionnel `@ManyToOne` et Mapping bidirectionnel `@OneToMany` et `@ManyToOne`:

Chacun des deux, a des avantages et des inconvénients :

- Le *mapping unidirectionnel* avec `@ManyToOne`, peut éviter le problème de performance potentiel d'un `@OneToMany`. <br>Mais il ne peut pas naviguer ou cascader les opérations CRUD vers les collections enfants vers la collection de base. Cependant, cela peut être fait manuellement.
- Le *mappage bidirectionnel* avec `@OneToMany` et `@ManyToOne` peut permettre aux deux entités de la relation d'accéder rapidement et de réaliser des opérations CRUD en cascade. <br>Cependant, cela peut causer un problème de performance sur une grande collection d'enfants.

Selon ces deux comparaisons, nous avons décidé d'utiliser le *mapping unidirectionnel*, car nous préférons éviter les problèmes de performance. Et pour la cascade, nous allons la faire manuellement.

## IV. Gestion des ressources <a name ="ressources"></a>

#### A. Schémas URL <a name ="surl"></a>

Pour l'API, nous avons utilisé REST avec les méthodes `GET`, `POST`, `PUT`, et `DELETE`.

### 1. Pour gérer les utilisateurs (Création, Affichage)

- **Pour créer un utilisateur**

```http request
POST http://localhost:8080/api/users/add
```

```http request
## Exemple

POST http://localhost:8080/api/add
Content-Type: application/json

{
  "pseudo":"ssgueye"
}
```

- **Pour afficher tous les utilisateurs**

```http request
GET http://localhost:8080/api/users
```

- **Pour afficher un utilisateur (en se basant sur son pseudo)**

```http request
GET http://localhost:8080/api/users/{{pseudo}}
```

```http request
## Exemple

GET http://localhost:8080/api/users/ssgueye
```

### 2. Pour gérer les séries (Création, Affichage, Modification et Suppression)

- **Pour créer une série**

```http request
POST http://localhost:8080/api/series/add/{{pseudo}}
```

```http request
## Exemple

POST http://localhost:8080/api/series/add/ssgueye
Content-Type: application/json

{
  "title": "Météo Clermont-Ferrand",
  "description": "Visualisez toute la météo de Clermont-Ferrand"
}
```

- **Pour afficher toutes les séries (créées et partagées) d'un utilisateur**

```http request
GET http://localhost:8080/api/series/{{pseudo}}
```

```http request
##Exemple: Afficher toutes les séries créées par "ssgueye" et partagées à "ssgueye"

GET http://localhost:8080/api/series/ssgueye
```

- **Pour afficher toutes les séries créées par un utilisateur**

```http request
GET http://localhost:8080/api/series/OwnSeries/{{pseudo}}
```

```http request
## Exemple: Afficher toutes les séries créées par "ssgueye"

GET http://localhost:8080/api/series/OwnSeries/ssgueye
```

- **Pour afficher toutes les séries qui nous ont été partagées**

```http request
GET http://localhost:8080/api/series/sharedSeries/{{pseudo}}
```

```http request
## Exemple: Afficher toutes les séries partagées à "ssgueye"

GET http://localhost:8080/api/series/sharedSeries/ssgueye
```

- **Pour modifier une série**

```http request
PUT http://localhost:8080/api/series/update/{{serie_id}}/{{pseudo}}
```

```http request
## Exemple: modifier la série avec l'id 1 de "ssgueye"

PUT http://localhost:8080/api/series/update/1/ssgueye
Content-Type: application/json

{
  "title": "Météo Clermont-Ferrand Semaine 46",
  "description": "Visualisez toute la météo de Clermont-Ferrand"
}
```

- **Pour supprimer une série**

```http request
DELETE http://localhost:8080/api/series/delete/{{serie_id}}/{{pseudo}}
```

```http request
## Exemple: supprimer la série avec l'id 1 de "ssgueye"

DELETE http://localhost:8080/api/series/delete/1/ssgueye
```

### 3. Pour gérer les séries et les permissions (Partage & Affichage)

- **Pour partager une série**

```http request
POST http://localhost:8080/api/user_series/share/{{serie_id}}/{{pseudoOwner}}/{{pseudoReceiver}}?permission={{permission}}
```

```http request
## Exemple: Le user "ssgueye" partage la série 1 à "dapieu" en mode "READONLY"

POST http://localhost:8080/api/user_series/share/1/ssgueye/dapieu?permission=READONLY
```

<strong style="color: red">Précision 1:</strong><span> Si l'utilisateur qui partage la série n'est pas le propriétaire de cette série, il y aura une erreur 500 qui précise que l'utilisateur n'a pas la permission de partager cette série.</span><br>
<strong style="color: red">Précision 2:</strong><span> Si l'utilisateur partage la série à lui même, on lui retourne la série sans rien modifier.</span><br>
<strong style="color: red">Précision 3:</strong><span> Si l'utilisateur partage deux fois la même série à un utilisateur, on vérifie s'il a changé le mode partage ou non. Si oui on met à jour le mode de partage. Sinon on garde l'ancien mode.</span><br>

- **Pour afficher une série et sa permission**

```http request
GET http://localhost:8080/api/user_series/{{pseudo}}/{{serie_id}}
```

```http request
## Exemple: Afficher la série avec id "1" de "ssgueye" et sa permission.

GET http://localhost:8080/api/user_series/ssgueye/1

```

- **Pour afficher toutes les séries et leur permission**

```http request
GET http://localhost:8080/api/user_series/{{pseudo}}
```

```http request
## Exemple: Afficher toutes les séries de "ssgueye" et leur permission

GET http://localhost:8080/api/user_series/ssgueye
```

### 4. Pour gérer les events (Création, Affichage, Modification et Suppression)

- P**our ajouter un event dans une série**

```http request
POST http://localhost:8080/api/events/add?pseudo={{pseudo}}&serie_id={{serie_id}}
```

```http request
## Exemple: créer et Ajouter un event dans la série "1" de "ssgueye"

POST http://localhost:8080/api/events/add?pseudo=ssgueye&serie_id=1
Content-Type: application/json

{
  "event_date": "2022-11-27T15:00",
  "value": 12,
  "comment": "Du vent frais"
}
```

<strong style="color: red">Précision :</strong><span> Le format de la date est: `yyyy-mm-ddTHH:mm:ss`</span><br>

- **Pour afficher tous les events d'une série**

```http request
GET http://localhost:8080/api/events/all?pseudo={{pseudo}}&serie_id={{serie_id}}
```

```http request
## Exemple: Afficher tous les events de la série "1"

GET http://localhost:8080/api/events/all?pseudo=ssgueye&serie_id=1

```

- **Pour afficher un event d'une série**

```http request
GET http://localhost:8080/api/events/one?pseudo={{pseudo}}&serie_id={{serie_id}}&event_id={{event_id}}
```

```http request
## Exemple: Afficher l'event "1" de la serie "1" de "ssgueye"

GET http://localhost:8080/api/events/one?pseudo=ssgueye&serie_id=1&event_id=1
```

- **Pour modifier un event**

```http request
PUT http://localhost:8080/api/events/update?pseudo={{pseudo}}&serie_id={{serie_id}}&event_id={{event_id}}
```

```http request
## Exemple: Modifier l'event "1" de la série "1" de "ssgueye"

PUT http://localhost:8080/api/events/update?pseudo=ssgueye&serie_id=1&event_id=1
Content-Type: application/json

{
  "event_date": "2022-11-27T15:00",
  "value": 12,
  "comment": "Du vent"
}
```

- **Pour supprimer un event**

```http request
DELETE http://localhost:8080/api/events/delete?pseudo={{pseudo}}&serie_id={{serie_id}}&event_id={{event_id}}
```

```http request
## Exemple: Supprimer l'event "1" de la série "1" de "ssgueye"

DELETE http://localhost:8080/api/events/delete?pseudo=ssgueye&serie_id=1&event_id=1
```

### 5. Pour gérer les tags

- **Pour ajouter un tag à un event**

```http request
POST http://localhost:8080/api/tags/add?pseudo={{pseudo}}&serieId={{serieId}}&eventId={{eventId}}
```

```http request
## Exemple: ajouter le tag à l'event "1" de la série "1" créé par "ssgueye"

POST http://localhost:8080/api/tags/add?pseudo=ssgueye&serieId=1&eventId=1
Content-Type: application/json

{
  "label": "Nuageux"
}
```

- **Pour lister les tags d'un event**

```http request
GET http://localhost:8080/api/tags?pseudo=ssgueye&serieId=1&eventId=1
```

```http request
## Exemple: lister tous les tags de l'event "1" de la série "1" créé par "ssgueye"

GET http://localhost:8080/api/tags?pseudo={{pseudo}}&serieId={{serieId}}&eventId={{eventId}}
```

- **Pour modifier un tag**

```http request
PUT http://localhost:8080/api/tags/update?pseudo={{pseudo}}&serieId={{serieId}}&eventId={{eventId}}&tagId={{tagId}}
```

```http request
## Exemple: modifier le tag "1" de l'event "1" de la série "1" créé par "ssgueye"

PUT http://localhost:8080/api/tags/update?pseudo=ssgueye&serieId=1&eventId=1&tagId=1
Content-Type: application/json

{
  "label": "basket ball"
}
```

- **Pour supprimer un tag**

```http request
DELETE http://localhost:8080/api/tags/delete?pseudo={{pseudo}}&serieId={{serieId}}&eventId={{eventId}}&tagIdd={{tagIdd}}
```

```http request
## Exemple: supprimer le tag "1" de l'event "1" de la série "1" créé par "ssgueye"

DELETE http://localhost:8080/api/tags/delete?pseudo=ssgueye&serieId=1&eventId=1&tagIdd=1

```

#### B. Négociation de contenu  <a name ="ndc"></a>

Pour la négociation de contenu, l'application accepte que le `JSON` et le `XML`.
Dans le `pom.xml`, nous avons ajouté une dépendance pour accepter le `XML`.

```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
</dependency>
```

Exemple d'une méthode de `GET` et `POST` qui accepte le `XML`:

```http request
## Ajouter un utilisateur

POST http://localhost:8080/api/users/add
Content-Type: application/xml

<AppUserEntity>
    <pseudo>ssgueye</pseudo>
</AppUserEntity>
```

```http request
## Afficher tous les utilisateurs

GET http://localhost:8080/api/users
Accept:application/xml
```

#### C. Requêtes conditionnelles <a name ="rc"></a>
Dans le dossier `config`, se trouve le fichier qui fait la configuration des Etags, nous avons placé les Etags dans les séries, les events et les userSeries.
Un exemple d'utilisation de l'Etag:
```http request
## Requête pour lister toutes les séries créées par l'utilisateur "ssgueye"

GET http://localhost:8080/api/series/OwnSeries/ssgueye
```
Quand on appelle cette requête pour la première fois, on a cet en-tête de réponse  avec le body correspondant :
```http request
HTTP/1.1 200 
ETag: "0c772c0ef852e0d40b3b749d088400276"
Content-Type: application/json
Content-Length: 162
Date: Tue, 29 Nov 2022 21:17:53 GMT
Keep-Alive: timeout=60
Connection: keep-alive

[
  {
    "id_serie": 1,
    "title": "Météo Clermont-Ferrand",
    "description": "Visualisez toute la météo de Clermont-Ferrand",
    "lastUpdatedDate": "2022-11-29T22:16:58.872752"
  }
]
```
Et si on place dans le header la requête conditionnelle `If-Non-Match : "0c772c0ef852e0d40b3b749d088400276"` comme ceci :
```http request
## Requête pour lister toutes les séries créées par l'utilisateur "ssgueye"

GET http://localhost:8080/api/series/OwnSeries/ssgueye
If-None-Match: "0c772c0ef852e0d40b3b749d088400276"
```
On aura un code de réponse de type `304 Not Modifed`
```http request
HTTP/1.1 304 
ETag: "0c772c0ef852e0d40b3b749d088400276"
Date: Tue, 29 Nov 2022 21:56:14 GMT
Keep-Alive: timeout=60
Connection: keep-alive

<Response body is empty>
```
Cela signifie que la ressource n'a pas été modifié. Et Si elle est modifiée, un autre Etag va être généré.