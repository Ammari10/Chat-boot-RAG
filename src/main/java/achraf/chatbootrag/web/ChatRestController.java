package achraf.chatbootrag.web;

// Importation de la classe ChatAiService depuis le package achraf.chatbootrag.service
import achraf.chatbootrag.service.ChatAiService;

// Importation des annotations nécessaires pour créer un contrôleur REST avec Spring
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

// Annotation @RestController pour indiquer que cette classe est un contrôleur REST
@RestController
// Annotation @RequestMapping pour définir la route de base "/chat" pour ce contrôleur
@RequestMapping("/chat")
public class ChatRestController {

    // Déclaration d'une instance de ChatAiService pour gérer la logique métier
    private ChatAiService chatAiService;

    // Constructeur de la classe ChatRestController prenant ChatAiService en paramètre
    // L'injection de dépendance permet de fournir une instance de ChatAiService automatiquement
    public ChatRestController(ChatAiService chatAiService) {
        this.chatAiService = chatAiService;
    }

    // Méthode de gestion des requêtes GET sur l'URL "/chat/ask"
    @GetMapping(value = "/ask" ,produces = MediaType.TEXT_PLAIN_VALUE)
    public String ask(String question) {
        // Appelle la méthode ragChat du service ChatAiService pour obtenir une réponse à la question
        return chatAiService.ragChat(question);
    }
}
