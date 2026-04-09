FROM openjdk:17

WORKDIR /app

COPY . .

RUN javac jchat/*.java

CMD ["java", "jchat.Servidor"]
