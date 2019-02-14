package com.lambdaschool.languages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/languages")
public class LanguageController {
  private final LanguageRepository LANG_REPO; // why: we will be accessing the database
  private final RabbitTemplate RBMQ_TEMPLATE; // why: we will be working with messages

  public LanguageController(LanguageRepository langRepo, RabbitTemplate rbmqTemplate) {
    this.LANG_REPO = langRepo;
    this.RBMQ_TEMPLATE = rbmqTemplate;
  }

  /**
   * Get all languages
   *
   * @return A list of all languages
   */
  @GetMapping()
  public List<Language> all() {
    return LANG_REPO.findAll();
  }

  /**
   * Get a language by id
   *
   * @param id                          A language id
   * @return                            A single language
   * @throws LanguageNotFoundException  If invalid id is given
   */
  @GetMapping("/{id}")
  public Language one(@PathVariable Long id) {
    return LANG_REPO.findById(id)
            .orElseThrow(() -> new LanguageNotFoundException(id));
  }

  /**
   * Return the sum of the population field.
   *
   * Note: This takes a large amount of resources and the population does not change often. Good
   * candidate for caching -> Redis.
   */
  @GetMapping("/population")
  public ObjectNode sumPopulation() {
    List<Language> languages = LANG_REPO.findAll();
    Long total = 0L;

    for(Language l : languages) {
      total += l.getPopulation();
    }

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode totalPopulation = mapper.createObjectNode();
    totalPopulation.put("id", 0);
    totalPopulation.put("language", "total");
    totalPopulation.put("population", total);

    // Log
    LanguageLog message = new LanguageLog("Checked Total Population");
    RBMQ_TEMPLATE.convertAndSend(LanguagesApplication.QUEUE_NAME, message.toString());
    log.info("Message sent");

    return totalPopulation;
  }

  /**
   * Create a new language(s)
   *
   * @param newLanguages  A list of languages to create
   * @return              A list of all languages added
   */
  @PostMapping()
  public List<Language> create(@RequestBody List<Language> newLanguages) {
    return LANG_REPO.saveAll(newLanguages);
  }
}
