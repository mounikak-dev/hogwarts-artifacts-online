package com.learn.hogwartsartifactsonline.wizard;

import com.learn.hogwartsartifactsonline.artifact.Artifact;
import com.learn.hogwartsartifactsonline.artifact.ArtifactRepository;
import com.learn.hogwartsartifactsonline.artifact.utils.IdWorker;
import com.learn.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WizardService {

    private final WizardRepository wizardRepository;

    //private final IdWorker idWorker;

    private final ArtifactRepository artifactRepository;

    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
    }

    public List<Wizard> findAllWizards() {
        return this.wizardRepository.findAll();
    }

    public Wizard findWizardById(Integer wizardId) {
        return this.wizardRepository.findById(wizardId).orElseThrow(() -> new ObjectNotFoundException("Wizard",wizardId));
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
        ).orElseThrow(() -> new ObjectNotFoundException("Wizard",wizardId));
    }

    public void deleteWizard(Integer wizardId){
        Wizard deletedWizard = this.wizardRepository.findById(wizardId).orElseThrow(()->new ObjectNotFoundException("Wizard",wizardId));
        deletedWizard.removeAllArtifacts();
        this.wizardRepository.deleteById(wizardId);
    }

    @Transactional
    public void assignArtifactToWizard(Integer wizardId, String artifactId){
        Artifact artifactToBeAssigned =  this.artifactRepository.findById(artifactId).orElseThrow(() -> new ObjectNotFoundException("Artifact not found ",artifactId));
        Wizard wizard = this.wizardRepository.findById(wizardId).orElseThrow(() -> new ObjectNotFoundException("wizard not found ",wizardId));

        if(artifactToBeAssigned.getOwner() != null){
            artifactToBeAssigned.getOwner().removeArtifact(artifactToBeAssigned);
        }

        wizard.addArtifact(artifactToBeAssigned);
    }
}
