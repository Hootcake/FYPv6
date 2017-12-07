/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { FyPv6TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RecipeDetailComponent } from '../../../../../../main/webapp/app/entities/recipe/recipe-detail.component';
import { RecipeService } from '../../../../../../main/webapp/app/entities/recipe/recipe.service';
import { Recipe } from '../../../../../../main/webapp/app/entities/recipe/recipe.model';

describe('Component Tests', () => {

    describe('Recipe Management Detail Component', () => {
        let comp: RecipeDetailComponent;
        let fixture: ComponentFixture<RecipeDetailComponent>;
        let service: RecipeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FyPv6TestModule],
                declarations: [RecipeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RecipeService,
                    JhiEventManager
                ]
            }).overrideTemplate(RecipeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RecipeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RecipeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Recipe(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.recipe).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
