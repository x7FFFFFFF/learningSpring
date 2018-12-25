import { TestBed } from '@angular/core/testing';

import { RolesServiceService } from './roles-service.service';

describe('RolesServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RolesServiceService = TestBed.get(RolesServiceService);
    expect(service).toBeTruthy();
  });
});
