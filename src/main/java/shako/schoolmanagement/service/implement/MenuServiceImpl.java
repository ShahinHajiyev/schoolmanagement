package shako.schoolmanagement.service.implement;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.entity.Menu;
import shako.schoolmanagement.repository.MenuRepository;
import shako.schoolmanagement.service.inter.MenuService;

import java.util.List;

@Data
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuRepository menuRepository;
    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }
}
