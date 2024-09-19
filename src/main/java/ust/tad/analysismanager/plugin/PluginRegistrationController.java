package ust.tad.analysismanager.plugin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("plugin-registration")
public class PluginRegistrationController {

  @Autowired PluginRegistrationService pluginRegistrationService;

  /**
   * Registers a new plugin.
   *
   * @param pluginRegistrationRequest
   * @return a PluginRegistrationResponse containing the names of the request queue and the response
   *     exchange.
   */
  @PostMapping(consumes = "application/json", produces = "application/json")
  public PluginRegistrationResponse registerPlugin(
      @RequestBody PluginRegistrationRequest pluginRegistrationRequest) {
    return pluginRegistrationService.registerPlugin(pluginRegistrationRequest);
  }
}
