/*
 * Copyright The Athenz Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from 'react';
import styled from '@emotion/styled';
import InputLabel from '../denali/InputLabel';
import Input from '../denali/Input';
import RadioButtonGroup from '../denali/RadioButtonGroup';
import { colors } from '../denali/styles';
import Color from '../denali/Color';
import CheckBox from '../denali/CheckBox';

const SectionsDiv = styled.div`
    width: 100%;
    text-align: left;
    background-color: ${colors.white};
`;

const SectionDiv = styled.div`
    align-items: flex-start;
    display: flex;
    flex-flow: row nowrap;
    padding: 10px 30px;
`;

const ContentDiv = styled.div`
    flex: 1 1;
    margin-right: 10px;
`;

const StyledInputLabel = styled(InputLabel)`
    flex: 0 0 100px;
    margin-right: 2%;
`;

const StyledInput = styled(Input)`
    width: 500px;
`;

const StyledRadioButtonGroup = styled(RadioButtonGroup)`
    margin-top: 8px;
`;

const ErrorDiv = styled.div`
    margin-left: 155px;
`;

export default class AddRuleFormForRole extends React.Component {
    constructor(props) {
        super(props);
        this.ruleEffectChanged = this.ruleEffectChanged.bind(this);
        this.state = {
            effects: [
                {
                    label: 'Allow',
                    value: 'ALLOW',
                },
                {
                    label: 'Deny',
                    value: 'DENY',
                },
            ],
            selectedEffect: 'ALLOW',
            errorMessage: null,
            name: '',
            action: '',
            resource: '',
            case: false,
        };
    }

    ruleEffectChanged(evt) {
        this.setState({ selectedEffect: evt.target.value });
        this.props.onChange('effect', evt.target.value);
    }

    inputChanged(key, evt) {
        if (key == 'case') {
            this.setState({ [key]: evt.target.checked });
            this.props.onChange(key, evt.target.checked);
        } else {
            this.setState({ [key]: evt.target.value });
            this.props.onChange(key, evt.target.value);
        }
    }

    render() {
        let policyNameChanged = this.inputChanged.bind(this, 'name');
        let resourceChanged = this.inputChanged.bind(this, 'resource');
        let actionChanged = this.inputChanged.bind(this, 'action');
        let caseChanged = this.inputChanged.bind(this, 'case');
        let policy = ``;
        let id = this.props.id;
        if (this.props.isPolicy) {
            policy = (
                <SectionDiv>
                    <StyledInputLabel htmlFor='policy-name'>
                        Policy Name
                    </StyledInputLabel>
                    <ContentDiv>
                        <StyledInput
                            id='policy-name'
                            name='policy-name'
                            value={this.state.name}
                            onChange={policyNameChanged}
                            placeholder={'Policy Name'}
                        />
                    </ContentDiv>
                </SectionDiv>
            );
        }
        return (
            <SectionsDiv
                autoComplete={'off'}
                data-testid='add-rule-form-for-role'
            >
                {this.state.errorMessage && (
                    <ErrorDiv>
                        <Color name={'red600'}>{this.state.errorMessage}</Color>
                    </ErrorDiv>
                )}
                {policy}
                <SectionDiv>
                    <StyledInputLabel htmlFor='rule-effect'>
                        Rule Effect
                    </StyledInputLabel>
                    <ContentDiv>
                        <StyledRadioButtonGroup
                            name={'rule-effect-radio-group' + id}
                            inputs={this.state.effects}
                            selectedValue={this.state.selectedEffect}
                            onChange={this.ruleEffectChanged}
                        />
                    </ContentDiv>
                </SectionDiv>
                <SectionDiv>
                    <StyledInputLabel htmlFor='rule-action'>
                        Rule Action
                    </StyledInputLabel>
                    <ContentDiv>
                        <StyledInput
                            id={'rule-action' + id}
                            name={'rule-action' + id}
                            value={this.state.action}
                            onChange={actionChanged}
                            placeholder={'Rule Action'}
                        />
                    </ContentDiv>
                </SectionDiv>
                <SectionDiv>
                    <StyledInputLabel htmlFor='rule-resource'>
                        Rule Resource
                    </StyledInputLabel>
                    <ContentDiv>
                        <StyledInput
                            id={'rule-resource' + id}
                            name={'rule-resource' + id}
                            value={this.state.resource}
                            onChange={resourceChanged}
                            placeholder={'Rule Resource'}
                        />
                    </ContentDiv>
                </SectionDiv>
                <SectionDiv>
                    <ContentDiv>
                        <CheckBox
                            checked={this.state.case}
                            name={'checkbox-case-sensitive' + id}
                            id={'checkbox-case-sensitive' + id}
                            key={'checkbox-case-sensitive' + id}
                            label='Case Sensitive Action and Resource'
                            onChange={caseChanged}
                        />
                    </ContentDiv>
                </SectionDiv>
            </SectionsDiv>
        );
    }
}
