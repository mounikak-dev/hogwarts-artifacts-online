package com.learn.hogwartsartifactsonline.wizard.converter;

import com.learn.hogwartsartifactsonline.wizard.Wizard;
import com.learn.hogwartsartifactsonline.wizard.dto.WizardDto;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;


@Component
public class WizardDtoToWizardConverter implements Converter<WizardDto, Wizard> {


    @Override
    public Wizard convert(WizardDto source) {
        Wizard wizard = new Wizard();
        wizard.setId(source.id());
        wizard.setName(source.name());
        return wizard;
    }
}
