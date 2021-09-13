package turitorial.searchKeyHis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchKeeyHisController {
    @Autowired
    SearchKeyHisRepository searchKeyHisRepository;
}
