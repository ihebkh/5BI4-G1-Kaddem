package tn.esprit.spring.kaddem.controllers;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.ContratRequestModel;

import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.services.IContratService;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/contrat")
public class ContratRestController {
	IContratService contratService;
	// http://localhost:8089/Kaddem/contrat/retrieve-all-contrats
	@GetMapping("/retrieve-all-contrats")
	public List<Contrat> getContrats() {
		return   contratService.retrieveAllContrats();
	}
	// http://localhost:8089/Kaddem/contrat/retrieve-contrat/8
	@GetMapping("/retrieve-contrat/{contrat-id}")
	public Contrat retrieveContrat(@PathVariable("contrat-id") Integer contratId) {
		return contratService.retrieveContrat(contratId);
	}

	@PostMapping("/add-contrat")
	public ResponseEntity<Contrat> addContract(@RequestBody ContratRequestModel contratRequestModel) {
		// Convert the DTO to the persistent entity
		Contrat contrat = new Contrat();
		contrat.setDateDebutContrat(contratRequestModel.getDateDebutContrat());
		contrat.setDateFinContrat(contratRequestModel.getDateFinContrat());
		contrat.setSpecialite(Specialite.valueOf(contratRequestModel.getSpecialite()));
		contrat.setArchive(contratRequestModel.getArchive());
		contrat.setMontantContrat(contratRequestModel.getMontantContrat());

		// Pass the entity to the service to handle persistence
		Contrat savedContrat = contratService.addContrat(contrat);
		return ResponseEntity.ok(savedContrat);
	}




	// http://localhost:8089/Kaddem/contrat/remove-contrat/1
	@DeleteMapping("/remove-contrat/{contrat-id}")
	public void removeContrat(@PathVariable("contrat-id") Integer contratId) {
		contratService.removeContrat(contratId);
	}

	// http://localhost:8089/Kaddem/contrat/update-contrat
	@PutMapping("/update-contrat")
	public Contrat updateContrat(@RequestBody ContratRequestModel contratRequestModel) {
		// Retrieve the existing contract (you might need to use an ID field in the DTO for this)
		Contrat existingContrat = contratService.retrieveContrat(contratRequestModel.getidContrat());

		// Update the fields from ContratRequestModel
		existingContrat.setDateDebutContrat(contratRequestModel.getDateDebutContrat());
		existingContrat.setDateFinContrat(contratRequestModel.getDateFinContrat());
		existingContrat.setSpecialite(Specialite.valueOf(contratRequestModel.getSpecialite()));
		existingContrat.setArchive(contratRequestModel.getArchive());
		existingContrat.setMontantContrat(contratRequestModel.getMontantContrat());

		// Pass the updated entity to the service layer to handle persistence
		return contratService.updateContrat(existingContrat);
	}



	@PutMapping(value = "/assignContratToEtudiant/{idContrat}/{nomE}/{prenomE}")
	public Contrat assignContratToEtudiant(@PathVariable Integer idContrat,
										   @PathVariable String nomE,
										   @PathVariable String prenomE) {
		return contratService.affectContratToEtudiant(idContrat, nomE, prenomE);
	}


	//The most common ISO Date Format yyyy-MM-dd — for example, "2000-10-31".
		@GetMapping(value = "/getnbContratsValides/{startDate}/{endDate}")
		public Integer getnbContratsValides(@PathVariable(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
										  @PathVariable(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

			return contratService.nbContratsValides(startDate, endDate);
		}

    //Only no-arg methods may be annotated with @Scheduled
    @Scheduled(cron="0 0 13 * * *")//(cron="0 0 13 * * ?")(fixedRate =21600)
	@PutMapping(value = "/majStatusContrat")
	public void majStatusContrat (){
		contratService.retrieveAndUpdateStatusContrat();

	}

	//public float getChiffreAffaireEntreDeuxDate(Date startDate, Date endDate)

	@GetMapping("/calculChiffreAffaireEntreDeuxDate/{startDate}/{endDate}")
	public float calculChiffreAffaireEntreDeuxDates(@PathVariable(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
	@PathVariable(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

		return contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);
	}
}


