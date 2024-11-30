import { TestBed } from '@angular/core/testing';

import { ToLeetService } from './to-leet.service';

describe('ToLeetService', () => {
  let service: ToLeetService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ToLeetService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
