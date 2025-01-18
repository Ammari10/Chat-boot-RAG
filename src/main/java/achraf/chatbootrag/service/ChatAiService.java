package achraf.chatbootrag.service;

// Importation de la classe ChatClient depuis le package org.springframework.ai.chat.client
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.ai.chat.client.ChatClient;

// Importation de l'annotation Service pour indiquer que cette classe est un service Spring
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

// Annotation @Service pour indiquer que cette classe est un composant Spring géré par le conteneur IoC
@BrowserCallable// c est la meme de compenment ( je veudrai tout les methode qui se trouve dans une class je
// je veudrai les appeler directemnet via mon frontend,, automatiqement ilya
//vadinn qui va vous genere un service qui sappele egalment chatAi Service et
//qui contienr ces methide lq aui contient des methode RAGchat
@AnonymousAllowed// un acces anonyme  je nai pas forcement de passer via l autentification pour faire appel a cette fonction
public class ChatAiService {

    // Déclaration d'une instance de ChatClient utilisée pour interagir avec le modèle de chat AI
    private ChatClient chatClient;
    private VectorStore vectorStore;
    @Value("classpath:/Prompts/prompt-template.st")
    private Resource promptResource;
    // Constructeur de la classe ChatAiService prenant un ChatClient.Builder en paramètre
    public ChatAiService(ChatClient.Builder builder) {
        // Utilisation du builder pour construire une instance de ChatClient
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }
    // Méthode publique ragChat qui prend une question en entrée et retourne une réponse sous forme de String
    public String ragChat(String question) {
        List<Document> documents=vectorStore.similaritySearch(question);// si je demande a mon victore store donne moi la liste de document la recherche symenthique
        List<String> Context =documents.stream().map(Document::getContent).toList();// convertir une liste de String
        PromptTemplate promptTemplate = new PromptTemplate(promptResource);
       Prompt prompt  = promptTemplate.create(
               Map.of("Context",Context,"Question",question));
        // Utilisation de chatClient pour envoyer une requête au modèle de chat AI
        return chatClient.prompt(prompt)       // Commence une nouvelle session de prompt
                     // Ajoute la question de l'utilisateur au prompt
                .call()                  // Envoie la requête au serveur et attend la réponse
                .content();               // Récupère le contenu de la réponse
    }
}
