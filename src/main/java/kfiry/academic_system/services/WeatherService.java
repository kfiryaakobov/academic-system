package kfiry.academic_system.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    // הגדרת המפתח והעיר ישירות במחלקה
    private final String API_KEY = "2e7d2400f3e57a6f2a1ed632a6d52ad7";
    private final String COLLEGE_CITY = "Jerusalem"; // תוכל לשנות לעיר של המכללה שלך
    
    private final RestTemplate restTemplate = new RestTemplate();

    private double getTemperature() {
        try {
            // הכתובת המלאה נבנית ישירות כאן
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + COLLEGE_CITY + "&appid=" + API_KEY + "&units=metric";
            
            String jsonResponse = restTemplate.getForObject(url, String.class);
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);
            
            // ניווט בתוך ה-JSON לשליפת הטמפרטורה
            return root.path("main").path("temp").asDouble();
            
        } catch (Exception e) {
            System.out.println("שגיאה בשליפת מזג האוויר: " + e.getMessage());
            return 22.0; // טמפרטורת גיבוי למקרה של תקלה בחיבור
        }
    }

    public String getUserWeatherRecommendation() {
        double temp = getTemperature();

        // הלוגיקה שמחזירה את ההודעה לסטודנט
        if (temp < 15) {
            return " פחות מ - 15 מעלות. יש להצתייד בהתאם. יש היום " + temp + " מעלות";
        } else if (temp >= 15 && temp <= 24) {
            return " מזג אוויר חמים עונת מעבר נעימה! יש היום " + temp + " מעלות";
        } else {
            return " חם היום! אל תשכחו להביא בקבוק מים לשיעור, יש היום " + temp + " מעלות";
        }
    }
}
