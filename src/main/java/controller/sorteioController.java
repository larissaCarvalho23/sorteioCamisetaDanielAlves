package controller;
import models.sorteio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.sorteioRepository;
import java.util.List;
import java.util.Random;
@RestController
@RequestMapping("/sorteio")
public class sorteioController {
    @Autowired
    sorteioRepository sorteioRepository;
    @PostMapping("/atualiza")
    public ResponseEntity realizaSorteio() {
        List<sorteio> numerojaSorteado = sorteioRepository.findAll();
        sorteio novoValor = new sorteio();
        Boolean salvaNumero = false;
        try {
            if (numerojaSorteado != null) {
                while (salvaNumero == false) {
                    Random random = new Random();
                    for (sorteio dado : numerojaSorteado) {
                        if (dado.getNumeroSorteio() != random.nextInt()) {
                            novoValor.setNumeroSorteio(random.nextInt());
                            salvaNumero = true;
                        }
                    }
                }
                return ResponseEntity.ok().build();
            } else {
                Random random = new Random();
                novoValor.setNumeroSorteio(random.nextInt());
                return ResponseEntity.ok().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }





}
