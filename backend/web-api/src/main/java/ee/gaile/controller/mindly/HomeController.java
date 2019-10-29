package ee.gaile.controller.mindly;

import ee.gaile.repository.entity.mindly.Portfolio;
import ee.gaile.repository.mindly.PortfolioRepository;
import ee.gaile.service.BitfinexAccess;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/mindly")
public class HomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    private final PortfolioRepository portfolioRepository;
    private String mistake = null;
    private Portfolio portfolio = new Portfolio();
    private Map<String, BigDecimal> currencyValue = new HashMap<>();
    private Date date = new Date();

    @Autowired
    public HomeController(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    /**
     * @param model
     * @return home.html
     */
    @GetMapping("/home")
    public String home(Model model) {
        List<Portfolio> getPortfolio = (List<Portfolio>) portfolioRepository.findAll();
        try {
            currencyValue.put("Bitcoin", new BitfinexAccess("Bitcoin").getPrice());
            currencyValue.put("Ethereum", new BitfinexAccess("Ethereum").getPrice());
            currencyValue.put("Ripple", new BitfinexAccess("Ripple").getPrice());
        } catch (Exception e) {
            LOGGER.info("No response received from Bitfinex: {}", e.getMessage());
            currencyValue.put("Bitcoin", new BigDecimal(0));
            currencyValue.put("Ethereum", new BigDecimal(0));
            currencyValue.put("Ripple", new BigDecimal(0));
            mistake = "Sorry, the data is currently unavailable, try again later.";
        }
        model.addAttribute("currencyValue", currencyValue);
        model.addAttribute("getPortfolio", getPortfolio);
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("date", date);
        model.addAttribute("mistake", mistake);
        return "/mindly/home";
    }

    /**
     * @param portfolio
     * @param errors    validation
     * @param model
     * @return home.html
     */
    @PostMapping("/add-portfolio-item")
    public String addPortfolioItem(@Valid Portfolio portfolio, Errors errors, Model model) {
        if (errors.hasErrors()) {
            LOGGER.warn("Invalid user form data");
            List<Portfolio> getPortfolio = (List<Portfolio>) portfolioRepository.findAll();
            model.addAttribute("currencyValue", currencyValue);
            model.addAttribute("getPortfolio", getPortfolio);
            model.addAttribute("date", date);
            model.addAttribute("mistake", mistake);
            return "/mindly/home";
        }
        portfolioRepository.save(portfolio);
        return "redirect:/mindly/home";
    }

    /**
     * @param deleteItem delete by id
     * @return home.html
     */
    @PostMapping("/delete-item")
    public String delete(@RequestParam UUID deleteItem) {
        portfolioRepository.deleteById(deleteItem);
        return "redirect:/mindly/home";
    }
}