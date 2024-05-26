package org.egualpam.services.hotelmanagement.e2e;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;
import org.egualpam.services.hotelmanagement.shared.infrastructure.AbstractIntegrationTest;
import org.egualpam.services.hotelmanagement.shared.infrastructure.helpers.EventStoreTestRepository;
import org.egualpam.services.hotelmanagement.shared.infrastructure.helpers.HotelTestRepository;
import org.egualpam.services.hotelmanagement.shared.infrastructure.helpers.ReviewTestRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils.nextInt;

class CreateReviewFeature extends AbstractIntegrationTest {

    private static final String CREATE_REVIEW_REQUEST = """
            {
                "hotelId": "%s",
                "rating": "%d",
                "comment": "%s"
            }
            """;

    @Autowired
    private HotelTestRepository hotelTestRepository;

    @Autowired
    private ReviewTestRepository reviewTestRepository;

    @Autowired
    private EventStoreTestRepository eventStoreTestRepository;

    @Test
    void reviewShouldBeCreated() throws Exception {
        UUID hotelId = randomUUID();
        UUID reviewId = randomUUID();

        hotelTestRepository
                .insertHotel(
                        hotelId,
                        randomAlphabetic(5),
                        randomAlphabetic(10),
                        randomAlphabetic(5),
                        nextInt(50, 1000),
                        "www." + randomAlphabetic(5) + ".com"
                );

        mockMvc.perform(
                        post("/v1/reviews/{reviewId}", reviewId.toString())
                                .contentType(APPLICATION_JSON)
                                .content(
                                        String.format(
                                                CREATE_REVIEW_REQUEST,
                                                hotelId,
                                                nextInt(1, 5),
                                                randomAlphabetic(10))))
                .andExpect(status().isCreated());

        assertTrue(reviewTestRepository.reviewExists(reviewId));

        // TODO: Create a new RabbitMQ consumer for test
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMqContainer.getHost());
        factory.setPort(rabbitMqContainer.getAmqpPort());
        factory.setUsername(rabbitMqContainer.getAdminUsername());
        factory.setPassword(rabbitMqContainer.getAdminPassword());

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        channel.queueDeclare("hotelmanagement.reviews", false, false, false, null);

        DefaultConsumer testConsumer = new DefaultConsumer(channel) {
            private static final Logger logger = LoggerFactory.getLogger(DefaultConsumer.class);

            @Override
            public void handleDelivery(
                    String consumerTag,
                    Envelope envelope,
                    AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                logger.info("Received message: {}", message);
            }
        };

        channel.basicConsume("hotelmanagement.reviews", true, testConsumer);

        boolean autoAck = false;
        await().atMost(10, SECONDS).untilAsserted(() -> {
            GetResponse getResponse = channel.basicGet("hotelmanagement.reviews", autoAck);// Check if there is a message in the
            assertNotNull(getResponse);
        });

        assertTrue(eventStoreTestRepository.domainEventExists(reviewId, "domain.review.created.v1.0"));
    }
}
