package com.learn.hogwartsartifactsonline.wizard;

import com.learn.hogwartsartifactsonline.system.Result;
import com.learn.hogwartsartifactsonline.system.StatusCode;
import com.learn.hogwartsartifactsonline.wizard.converter.WizardDtoToWizardConverter;
import com.learn.hogwartsartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import com.learn.hogwartsartifactsonline.wizard.dto.WizardDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/wizards")
public class WizardController {

    private final WizardService wizardService;

    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

    private final WizardDtoToWizardConverter wizardDtoToWizardConverter;

    public WizardController(WizardService wizardService, WizardToWizardDtoConverter wizardToWizardDtoConverter, WizardDtoToWizardConverter wizardDtoToWizardConverter) {
        this.wizardService = wizardService;
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
    }

    @GetMapping
    public Result findAllWizards() {
        List<Wizard> wizards = this.wizardService.findAllWizards();
        List<WizardDto> wizardDtos = wizards.stream()
                .map(this.wizardToWizardDtoConverter::convert).collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find all wizards.", wizardDtos);
    }

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId) {
        Wizard wizard = this.wizardService.findWizardById(wizardId);
        WizardDto wizardDto = this.wizardToWizardDtoConverter.convert(wizard);
        return new Result(true, StatusCode.SUCCESS, "Find Wizard Success", wizardDto);
    }

    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto) {
        Wizard newWizard = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard savedWizard = this.wizardService.addNewWizard(newWizard);
        WizardDto savedWizardDto = this.wizardToWizardDtoConverter.convert(savedWizard);
        return new Result(true, StatusCode.SUCCESS, "Added new wizard", savedWizardDto);
    }

    @PutMapping("/{wizardId}")
    public Result updateWizard(@PathVariable Integer wizardId, @Valid @RequestBody WizardDto wizardDto) {
        Wizard update = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard updatedWizard = this.wizardService.updateWizard(wizardId, update);
        WizardDto updatedDto = this.wizardToWizardDtoConverter.convert(updatedWizard);
        return new Result(true, StatusCode.SUCCESS, "updated wizard successfully", updatedDto);
    }

    @DeleteMapping("/{wizardId}")
    public Result deleteWizard(@PathVariable Integer wizardId) {
        this.wizardService.deleteWizard(wizardId);
        return new Result(true, StatusCode.SUCCESS, "deleted successfully");
    }

}
