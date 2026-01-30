/**
 * N3RD M@RKET - Prediction Market for Collectables
 * Handles welcome messages and  user greetings.
 *
 * @author Hrushi Bhatt
 */

package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class HomePage {

    @GetMapping("/")
    public String getHomePage() {
        return "Welcome to N3RD M@RKET - Your prediction market for collectables!";
    }

    @GetMapping("/{name}")
    public String greetUser(@PathVariable String name) {
        return "Welcome to N3RD M@RKET - Your prediction market for collectables!: " + name;
    }

    @GetMapping("/about")
    public String getAboutInfo() {
        return "N3RD M@RKET: Predict prices on Comics, Trading Cards, Movies, and Games!";
    }
}