package tn.esprit.spring.kaddem.services;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;


@Slf4j
@Service
public class ContratServiceImpl implements IContratService{

	private final ContratRepository contratRepository;
	private final EtudiantRepository etudiantRepository;


	public ContratServiceImpl(ContratRepository contratRepository, EtudiantRepository etudiantRepository) {
		this.contratRepository = contratRepository;
		this.etudiantRepository = etudiantRepository;
	}

	public List<Contrat> retrieveAllContrats(){
		return  contratRepository.findAll();
	}

	public Contrat retrieveContrat (Integer  idContrat){
		return contratRepository.findById(idContrat).orElse(null);
	}

	public  void removeContrat(Integer idContrat){
		Contrat c=retrieveContrat(idContrat);
		contratRepository.delete(c);
	}


	public Contrat affectContratToEtudiant(Integer idContrat, String nomE, String prenomE) {
		Etudiant e = etudiantRepository.findByNomEAndPrenomE(nomE, prenomE);
		Contrat ce = contratRepository.findByIdContrat(idContrat);
		Set<Contrat> contrats = e.getContrats();
		Integer nbContratssActifs = 0;
		if (!contrats.isEmpty()) {  // Use isEmpty() to check if the collection is empty
			for (Contrat contrat : contrats) {
				if (Boolean.TRUE.equals(contrat.getArchive())) {  // Remove the unnecessary boolean literal
					nbContratssActifs++;
				}
			}
		}

		if (nbContratssActifs <= 4) {  // Covered code
			ce.setEtudiant(e);
			contratRepository.save(ce);
		}

		return ce;
	}


	public 	Integer nbContratsValides(Date startDate, Date endDate){
		return contratRepository.getnbContratsValides(startDate, endDate);
	}





}



