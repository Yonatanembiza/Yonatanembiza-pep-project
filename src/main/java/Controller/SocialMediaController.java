package Controller;

import java.util.List;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    // Instance variables
    private final MessageService messageService; // Dependency for handling messages
    private final AccountService accountService; // Dependency for handling user accounts
    private final ObjectMapper objectMapper;     // Dependency for JSON serialization/deserialization

    // Constructor for SocialMediaController
    public SocialMediaController() {
        // Initialize the dependencies
        messageService = new MessageService(); 
        accountService = new AccountService(); 
        objectMapper = new ObjectMapper();
    }

    /**
     * Method for starting the API
     * 
     * Initializes a Javalin instance, sets up the routes for handling HTTP requests,
     * and returns the initialized Javalin instance.
     *
     * @return the initialized Javalin instance
     */
    public Javalin startAPI() {

        // Create a new Javalin instance
        Javalin app = Javalin.create();

        // Register routes for handling HTTP requests

        // Register a new user
        app.post("/register", this::registerUserHandler);
        // User login
        app.post("/login", this::loginUserHandler);
        // Create a new message
        app.post("/messages", this::createMessageHandler);
        // Retrieve all messages
        app.get("/messages", this::getAllMessagesHandler);
        // Retrieve a message by message ID
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        // Retrieve all messages by account ID
        app.get("/accounts/{account_id}/messages", this::getMessageByUserIdHandler);
        // Delete message by message ID
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        // Update a message by message ID
        app.patch("/messages/{message_id}", this::updateMessageByIDHandler);

        // Return the initialized Javalin instance
        return app;
    }

    /**
     * Handles the creation of a new message.
     *
     * @param ctx the Javalin context
     */
    private void createMessageHandler(Context ctx) {
        try {
            // Deserialize the request body JSON into a Message object
            Message message = objectMapper.readValue(ctx.body(), Message.class);
            // Call the messageService to create the message
            Message createdMessage = messageService.createMessage(message);
            if (createdMessage != null) {
                // Set the response body JSON to the created message
                ctx.json(createdMessage).status(200);
            } else {
                ctx.status(400);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            ctx.status(400);
        }
    }

    /**
     * Retrieves all messages.
     *
     * @param ctx the Javalin context
     */
    private void getAllMessagesHandler(Context ctx) {
        // Call the messageService to get all messages
        List<Message> messages = messageService.getAllMessages();
        if (messages != null) {
            // Set the response body JSON to the list of messages
            ctx.json(messages).status(200);
        } else {
            ctx.status(200).result("");
        }
    }

    /**
     * Retrieves a message by its message ID.
     *
     * @param ctx the Javalin context
     */
    private void getMessageByIdHandler(Context ctx) {
        // Get the message ID from the URL path parameter
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        // Call the messageService to get the message by its ID
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            // Set the response body JSON to the retrieved message
            ctx.json(message).status(200);
        } else {
            ctx.status(200).result("");
        }
    }

    /**
     * Deletes a message by its message ID.
     *
     * @param ctx the Javalin context
     */
    private void deleteMessageByIDHandler(Context ctx) {
        // Get the message ID from the URL path parameter
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        // Call the messageService to delete the message by its ID
        Message deletedMessage = messageService.deleteMessage(messageId);

        if (deletedMessage != null) {
            // Set the response body JSON to the now deleted message
            ctx.json(deletedMessage).status(200);
        } else {
            ctx.status(200).result("");
        }
    }

    /**
     * Updates a message by its message ID.
     *
     * @param ctx the Javalin context
     */
    private void updateMessageByIDHandler(Context ctx) {
        try {
            // Get the message ID from the URL path parameter
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            // Deserialize the request body JSON into a Message object
            Message message = objectMapper.readValue(ctx.body(), Message.class);
            // Call the messageService to update the message by its ID
            Message updatedMessage = messageService.updateMessageText(message.getMessage_text(), messageId);
            if (updatedMessage != null) {
                // Set the response body JSON to the updated message
                ctx.json(updatedMessage).status(200);
            } else {
                ctx.status(400);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            ctx.status(400);
        }
    }

    /**
     * Handles the registration of a new user.
     *
     * @param ctx the Javalin context
     */
    private void registerUserHandler(Context ctx) {
        try {
            // Deserialize the request body JSON into an Account object
            Account account = objectMapper.readValue(ctx.body(), Account.class);
            // Call the accountService to register the user account
            Account registeredAccount = accountService.registerAccount(account.getUsername(), account.getPassword());
            if (registeredAccount != null) {
                // Set the response body JSON to the registered account
                ctx.json(registeredAccount).status(200);
            } else {
                ctx.status(400);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            ctx.status(400);
        }
    }

    /**
     * Handles user login.
     *
     * @param ctx the Javalin context
     */
    private void loginUserHandler(Context ctx) {
        try {
            // Deserialize the request body JSON into an Account object
            Account account = objectMapper.readValue(ctx.body(), Account.class);
            // Call the accountService to perform user login
            Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
            if (loggedInAccount != null) {
                // Set the response body JSON to the logged-in account
                ctx.json(loggedInAccount).status(200);
            } else {
                ctx.status(401);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            ctx.status(400);
        }
    }

    /**
     * Retrieves all messages posted by a specific user using account ID.
     *
     * @param ctx the Javalin context
     */
    private void getMessageByUserIdHandler(Context ctx) {
        // Get the account ID from the URL path parameter
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        // Call the messageService to get messages by account ID
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        if (messages != null) {
            // Set the response body JSON to the list of messages
            ctx.json(messages).status(200);
        } else {
            ctx.status(200).result("");
        }
    }
}
