package com.sacavix.chatgpt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.ui.Model;

import io.github.flashvayne.chatgpt.dto.ChatRequest;
import io.github.flashvayne.chatgpt.dto.ChatResponse;
import io.github.flashvayne.chatgpt.service.ChatgptService;

@ComponentScan("io.github.flashvayne.chatgpt")

@Controller
@SessionAttributes("chatEntries")
public class HelloGPTController {

    @Autowired
    private ChatgptService chatgptService;

    // Método para inicializar la lista de chatEntries si no existe en la sesión
    @ModelAttribute("chatEntries")
    public List<ChatEntry> chatEntries() {
        return new ArrayList<>();
    }

    @GetMapping("/")
    public String holaMundo(Model model) {
        // Obtener la lista de chatEntries de la sesión
        @SuppressWarnings("unchecked")
        List<ChatEntry> chatEntries = (List<ChatEntry>) model.getAttribute("chatEntries");

        // Pasar la lista al modelo para que esté disponible en la vista
        model.addAttribute("chatEntries", chatEntries);

        return "chat";
    }

    @GetMapping("/chat")
    public String chatWith(@RequestParam String message, Model model) {
        System.out.println(message);

        // Llama al servicio para obtener la respuesta basada en el mensaje.
        String response = chatgptService.sendMessage(message);

        // Obtener la lista de chatEntries de la sesión
        @SuppressWarnings("unchecked")
        // Obtener la lista de chatEntries de la sesión
        List<ChatEntry> chatEntries = (List<ChatEntry>) model.getAttribute("chatEntries");

        // Verificar si la lista aún no se ha inicializado en la sesión
        if (chatEntries == null) {
            chatEntries = new ArrayList<>();
            model.addAttribute("chatEntries", chatEntries);
        }

        // Crear una nueva entrada de chat con la pregunta y la respuesta
        ChatEntry chatEntry = new ChatEntry(message, response);

        // Agregar la entrada de chat a la lista
        chatEntries.add(chatEntry);

        // Agregar el mensaje al modelo para que esté disponible en la vista.
        model.addAttribute("message", message);

        // Devuelve el nombre de la vista que quieres mostrar ("chat").
        return "chat";
    }

    //Prompt
    @GetMapping("/prompt")
    public String prompt(){
        return "chat_prompt";
    }

    @GetMapping("/chat_prompt")
    public String prompt(@RequestParam String message, Model model) {
        Integer maxTokens = 300;
        String modelo = "text-davinci-003";
        Double temperature = 0.5;
        Double topP = 1.0;
    
        ChatRequest chatRequest = new ChatRequest(modelo, message, maxTokens, temperature, topP);
        ChatResponse res = chatgptService.sendChatRequest(chatRequest);
    
        // Obtener solo el texto de la respuesta
        String chatResponseText = res.getChoices().get(0).getText();
    
        // Formatear el texto para dividirlo en párrafos
        String[] paragraphs = chatResponseText.split("\\d+\\.");
    
        // Agregar los párrafos al modelo para pasarlos a la vista
        model.addAttribute("chatResponseParagraphs", paragraphs);
    
        // Devolver el nombre de la vista
        return "chat_prompt";
    }
    
}


