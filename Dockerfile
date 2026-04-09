FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN javac jchat/*.java

CMD ["java", "jchat.Servidor"]
