package ust.tad.analysismanager.analysistask;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    
    @Autowired
    private LocationRepository locationRepository;
    
    
    public Location createLocation(URL url) {
        Location newLocation = new Location();
        newLocation.setUrl(url);
        return locationRepository.save(newLocation);
    }

    public List<Location> saveLocations(List<Location> locations) {
        return locationRepository.saveAll(locations);
    }

    public List<Location> duplicateLocations(List<Location> locations) {
        List<Location> duplicatedLocations = new ArrayList<>();
        for (Location location : locations) {
            duplicatedLocations.add(
                new Location(location.getUrl(), 
                location.getStartLineNumber(), 
                location.getEndLineNumber()));
        }
        return locationRepository.saveAll(duplicatedLocations);
    }
    
}
