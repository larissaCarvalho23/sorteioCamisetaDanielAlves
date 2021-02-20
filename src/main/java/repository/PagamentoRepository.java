package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import models.Pagamento;



public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

}
