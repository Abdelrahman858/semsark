package project.semsark.service.user_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.semsark.exception.HelperMessage;
import project.semsark.jwt.JwtUtil;
import project.semsark.model.entity.Building;
import project.semsark.model.entity.Favourites;
import project.semsark.model.entity.User;
import project.semsark.repository.BuildingRepository;
import project.semsark.repository.FavouriteRepository;
import project.semsark.repository.UserRepository;

import javax.swing.plaf.ButtonUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavouriteService {
    @Autowired
    FavouriteRepository favouriteRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    JwtUtil jwtUtil;

    public void addToFavourite(long id){
        User user = jwtUtil.getUserDataFromToken();
        Optional<Building> building =buildingRepository.findById(id);

        if(building.isPresent()){
            List<Building>list=new ArrayList<>();
            list.add(building.get());

            Favourites fav=new Favourites();
            fav.setBuildings(list);
            fav.setUserId(user.getId());

            favouriteRepository.save(fav);
            user.getFavourites().add(fav);
            userRepository.save(user);
        }else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, HelperMessage.BUILDING_NOT_FOUND);

    }

    public void deleteFavourite(long id){
        User user = jwtUtil.getUserDataFromToken();
        Optional<Favourites> fav = favouriteRepository.findById(id);
        if(fav.isPresent()) {
            user.getFavourites().remove(fav.get());
            favouriteRepository.deleteById(id);
        }else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,HelperMessage.Favourite_NOT_FOUND);
    }
    ////////////////////////////////////////////////////////////////
    public List<Favourites> getMyFavourites(){
        User user=jwtUtil.getUserDataFromToken();
        return user.getFavourites();
    }
}
