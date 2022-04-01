package ust.tad.analysismanager.analysistask;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    
    @Autowired
    private LocationRepository location1Repository;
    
    
    public Location createLocation(URL url) {
        Location newLocation = new Location();
        newLocation.setUrl(url);
        return location1Repository.save(newLocation);
    }
    
}
