package achraf.chatbootrag.service;

// Importation de la classe ChatClient depuis le package org.springframework.ai.chat.client
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.ai.chat.client.ChatClient;

// Importation de l'annotation Service pour indiquer que cette classe est un service Spring
import org.springframework.stereotype.Service;

// Annotation @Service pour indiquer que cette classe est un composant Spring géré par le conteneur IoC
@BrowserCallable
@AnonymousAllowed
public class ChatAiService {

    // Déclaration d'une instance de ChatClient utilisée pour interagir avec le modèle de chat AI
    private ChatClient chatClient;

    // Constructeur de la classe ChatAiService prenant un ChatClient.Builder en paramètre
    public ChatAiService(ChatClient.Builder builder) {
        // Utilisation du builder pour construire une instance de ChatClient
        this.chatClient = builder.build();
    }

    // Méthode publique ragChat qui prend une question en entrée et retourne une réponse sous forme de String
    public String ragChat(String question) {
        // Utilisation de chatClient pour envoyer une requête au modèle de chat AI
        return chatClient.prompt()       // Commence une nouvelle session de prompt
                .user(question)          // Ajoute la question de l'utilisateur au prompt
                .call()                  // Envoie la requête au serveur et attend la réponse
                .content();              // Récupère le contenu de la réponse
    }
}
