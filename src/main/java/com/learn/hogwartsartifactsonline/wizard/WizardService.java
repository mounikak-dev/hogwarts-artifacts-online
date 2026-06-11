package com.learn.hogwartsartifactsonline.wizard;

import com.learn.hogwartsartifactsonline.artifact.utils.IdWorker;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WizardService {

    private final WizardRepository wizardRepository;

    //private final IdWorker idWorker;

    public WizardService(WizardRepository wizardRepository, IdWorker idWorker) {
        this.wizardRepository = wizardRepository;
        //this.idWorker = idWorker;
    }

    public List<Wizard> findAllWizards() {
        return this.wizardRepository.findAll();
    }

    public Wizard findWizardById(Integer wizardId) {
        return this.wizardRepository.findById(wizardId).orElseThrow(() -> new WizardNotFoundException(wizardId));
    }

    public Wizard addNewWizard(Wizard wizard) {
        // wizard.setId((int) idWorker.nextId());
        return this.wizardRepository.save(wizard);
    }

    public Wizard updateWizard(Integer wizardId, Wizard wizard) {
        return this.wizardRepository.findById(wizardId).map(
                oldWizard -> {
                    oldWizard.setName(wizard.getName());
                    return this.wizardRepository.save(oldWizard);
                }
        ).orElseThrow(() -> new WizardNotFoundException(wizardId));
    }

    public void deleteWizard(Integer wizardId){
        Wizard deletedWizard = this.wizardRepository.findById(wizardId).orElseThrow(()->new WizardNotFoundException(wizardId));
        deletedWizard.removeAllArtifacts();
        this.wizardRepository.deleteById(wizardId);
    }

    public void assignArtifactToWizard(Integer wizardId, String artifactId){

    }
}
