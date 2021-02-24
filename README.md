[![Build Status](https://travis-ci.org/sukolenvo/streamlabs-extension.svg?branch=master)](https://travis-ci.org/sukolenvo/streamlabs-extension)

### Streamlabs Extension

Twitch integration application that allows to show Streamlabs alerts
by entering commands in Twitch chat.

#### API

[Twitch Chat API](https://dev.twitch.tv/docs/v5/reference/chat/)

[Streamlabs API](https://dev.streamlabs.com/)

#### Technologies

Java 8, Spring boot, JPA, AngularJS, Pircbotx as IRC client, Lombook.

#### Configuration and Run

1. Fill in [application.properties](src/main/resources/application.properties);
2. Run 
[TwitchBotApplication.java](src/main/java/com/dakare/streamlabs/TwitchBotApplication.java) from your IDE or
run `mvnw spring-boot:run`.
3. Open in browser http://localhost:4030

#### Customise

1. Update support email in [404.html](src/main/resources/static/404.html) and [500.html](src/main/resources/static/500.html).
2. Its recommended to use SQL database (e.g. PostgreSQL) on production instead on default configured in-memory, that is a good fit for testing.

#### Debug

To debug project - enable annotation processor in settings of your IDE.