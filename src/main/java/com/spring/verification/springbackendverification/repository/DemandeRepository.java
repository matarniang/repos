package com.spring.verification.springbackendverification.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.verification.springbackendverification.model.Demande;

@Repository
@Transactional(readOnly = true)
public interface DemandeRepository extends JpaRepository<Demande, Long>{

      @Transactional
      @Modifying
      @Query(value="SELECT * FROM Demande p WHERE login=?1 AND satus='i'",nativeQuery =true)
      List<Demande> GetDemandeI(String login);
      
      @Query(value="SELECT * FROM Demande p WHERE login=?1 AND satus='e'",nativeQuery =true)
      List<Demande> GetDemandeE(String login);
      
      
      @Query(value="SELECT * FROM Demande p WHERE login=?1",nativeQuery =true)
      List<Demande> GetDemande(String login);
      


}





