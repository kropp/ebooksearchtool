from djangosphinx.models import SphinxSearch

from django.db import models

from queryspell.trigram_tool import generate_trigram

DICTIONARY_NAME_LENGTH = 255
WORD_LENGTH = 255
TRIGRAM_LENGTH = 3
TRIGRAM_FIELD_LENGTH = WORD_LENGTH * (TRIGRAM_LENGTH + 1)

YES_NO_CHOICES = (
    ('y', 'yes'),
    ('n', 'no'),
)

class Dictionary(models.Model):
    name = models.CharField(max_length=DICTIONARY_NAME_LENGTH, unique=True)
    indexed_status = models.CharField(max_length=1, choices=YES_NO_CHOICES, default='n')

    def is_indexed(self):
        return self.indexed_status == 1
    
    def correct(self, query):
        trigrams = u' '.join(generate_trigram(query))
        query_length = len(query)
        print trigrams
        query_extended = '"%s"/2' % trigrams
        results = Words.search.query(query_extended)
        results = results.filter(dict_id=self.id)
        #results = results.order_by('@weight', '-frequency')
        #results = results.order_by('@weight')
        return results
        

    def __unicode__(self):
        return self.name

  


class Words(models.Model):
    word = models.CharField(max_length=WORD_LENGTH)
    trigrams = models.CharField(max_length=TRIGRAM_FIELD_LENGTH)

    length = models.IntegerField()
    frequency = models.IntegerField()
    dictionary = models.ForeignKey(Dictionary)

    class Meta:
        unique_together = ('word', 'dictionary')

    def __unicode__(self):
        string = "'%s' [freq %s] [dict %s]" % \
            (self.word, self.frequency, self.dictionary.name,)
        return string

    search = SphinxSearch(
        index='queryspell_index',
        mode='SPH_MATCH_EXTENDED2',
        rankmode='SPH_RANK_WORDCOUNT',
    )


#Dictionary.get(name='title').correct('liight')
