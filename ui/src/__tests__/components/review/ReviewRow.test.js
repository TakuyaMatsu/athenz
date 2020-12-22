/*
 * Copyright 2020 Verizon Media
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
import { render } from '@testing-library/react';
import ReviewRow from '../../../components/review/ReviewRow';
import { colors } from '../../../components/denali/styles';

describe('ReviewRow', () => {
    it('should render', () => {
        let details = {
            memberName: 'user.member',
        };
        let domain = 'domain';
        let role = 'roleName';
        let color = colors.row;
        let idx = '50';
        const { getByTestId } = render(
            <table>
                <tbody>
                    <ReviewRow
                        details={details}
                        domain={domain}
                        role={role}
                        color={color}
                        idx={idx}
                    />
                </tbody>
            </table>
        );
        const reviewRow = getByTestId('review-row');

        expect(reviewRow).toMatchSnapshot();
    });
});